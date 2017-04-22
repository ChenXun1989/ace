package wiki.chenxun.ace.core.base.common;

import wiki.chenxun.ace.core.base.annotations.ConfigBean;
import wiki.chenxun.ace.core.base.annotations.Spi;
import wiki.chenxun.ace.core.base.config.Config;
import wiki.chenxun.ace.core.base.config.ConfigBeanAware;
import wiki.chenxun.ace.core.base.config.ConfigBeanParser;
import wiki.chenxun.ace.core.base.config.DefaultConfig;

import wiki.chenxun.ace.core.base.exception.ExtendLoadException;

import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扩展点加载类
 *
 * @param <T> spi扩展点
 */
public final class ExtendLoader<T> {

    /**
     * 扩展类加载全局容器
     */
    private static ConcurrentHashMap<Class<?>, ExtendLoader<?>> extendLoaderMap;

    static {
        extendLoaderMap = new ConcurrentHashMap<>();
    }

    /**
     * spi 默认值
     */
    public static final String DEFAULT_SPI_NAME = "default";
    /**
     * spi 文件位置
     */
    private static final String ACE_DIRECTORY = "META-INF/ace/";

    /**
     * 扩展点多个扩展实例容器
     */
    private final ConcurrentHashMap<String, Object> extendInstances;
    /**
     * 一个扩展点对应的多个扩展class
     */
    private final ConcurrentHashMap<String, Class> extendClasses;
    /**
     * 扩展点对应的默认扩展
     */
    private String cachedDefaultName;
    /**
     * 扩展点类型
     */
    private Class type;

    private ExtendLoader(Class type) {
        this.type = type;
        extendInstances = new ConcurrentHashMap<>();
        extendClasses = new ConcurrentHashMap<>();
    }


    /**
     * 获取扩展点的扩展加载类
     *
     * @param type 扩展点类型
     * @param <T>  类型
     * @return 扩展加载类
     */
    public static <T> ExtendLoader<T> getExtendLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("spi only support interface ");
        }
        if (!withExtensionAnnotation(type)) {
            throw new ExtendLoadException("class must has @spi  ");
        }

        ExtendLoader loader = extendLoaderMap.get(type);
        if (loader == null) {
            extendLoaderMap.putIfAbsent(type, new ExtendLoader(type));
            loader = (ExtendLoader) extendLoaderMap.get(type);
        }

        return loader;
    }

    /**
     * 比较class是否有@spi注解
     *
     * @param type Class
     * @param <T>  范型
     * @return 有返回true，否则fasle
     */
    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(Spi.class);
    }

    /**
     * 获取扩展点类加载器
     *
     * @return ClassLoader
     */
    private static ClassLoader findClassLoader() {
        return ExtendLoader.class.getClassLoader();
    }

    /**
     * 获取扩展点实例
     *
     * @param name 扩展点的key
     * @return 扩展点实例
     */
    public T getExtension(String name) {
        if (cachedDefaultName == null) {
            this.loadExtensionClasses();
        }
        if (name == null || name.trim().length() == 0) {
            name = cachedDefaultName;
        }
        if (DEFAULT_SPI_NAME.equals(name)) {
            name = this.cachedDefaultName;
        }
        T t = (T) this.extendInstances.get(name);
        if (t == null) {
            Class cls = extendClasses.get(name);
            this.extendInstances.putIfAbsent(name, createExtension(name));
            t = (T) extendInstances.get(name);
        }
        return t;
    }

    /**
     * 创建扩展点实例
     *
     * @param name 扩展点的key
     * @return 扩展点实例
     */
    private T createExtension(String name) {
        Class clazz = this.extendClasses.get(name);
        if (clazz == null) {
            throw new ExtendLoadException("extend " + name + " not find ");
        }
        try {
            T t = (T) clazz.newInstance();
            injectConfigBean(t);
            // injectProperty(t);
            return t;
        } catch (Exception e) {
            throw new ExtendLoadException("extend instance fail  ", e);
        }
    }

    /**
     * 注入configBean
     *
     * @param object
     */
    private void injectConfigBean(Object object) {

        Config config = DefaultConfig.INSTANCE;
        Type[] types = this.type.getGenericInterfaces();
        for (Type interfaceTyp : types) {
            if (interfaceTyp instanceof ParameterizedType) {
                Type t = ((ParameterizedType) interfaceTyp).getRawType();
                if (ConfigBeanAware.class.getName().equals(t.getTypeName())) {
                    Type a = ((ParameterizedType) interfaceTyp).getActualTypeArguments()[0];
                    try {
                        Class cls = Class.forName(a.getTypeName());
                        ConfigBeanParser configBeanParser = config.configBeanParser(cls);
                        if (configBeanParser == null) {
                            configBeanParser = new ConfigBeanParser();
                            if (cls.isAnnotationPresent(ConfigBean.class)) {
                                configBeanParser.parser(cls);
                                config.add(configBeanParser);
                            } else {
                                throw new ExtendLoadException(cls + "  must has @ConfigBean ");
                            }
                        }
                        Method method = ConfigBeanAware.class.getMethod("setConfigBean",Object.class);
                        method.invoke(object, configBeanParser.getConfigBean());
                        configBeanParser.addObserver((Observer) object);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    /**
     * 给扩展点实例注入ioc容器对象
     *
     * @param t   扩展点实例
     * @param <T> 扩展点
     * @throws IntrospectionException    IntrospectionException
     * @throws InvocationTargetException InvocationTargetException
     * @throws IllegalAccessException    IllegalAccessException
     */
    private <T> void injectProperty(T t) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
//        Container container = Context.getCurrentContainer();
//        BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
//        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
//        if (pds != null && pds.length > 0) {
//            for (PropertyDescriptor pd : pds) {
//                Method method = pd.getWriteMethod();
//                if (method == null) {
//                    continue;
//                }
//                Object obj = container.getBean(pd.getPropertyType(), pd.getName());
//                method.invoke(t, obj);
//            }
//        }

    }

    /**
     * 加载扩展点对应的class列表
     */
    private void loadExtensionClasses() {
        Spi defaultAnnotation = (Spi) this.type.getAnnotation(Spi.class);
        if (defaultAnnotation != null) {
            this.cachedDefaultName = defaultAnnotation.value();
        }
        this.loadFile(ACE_DIRECTORY);
    }

    /**
     * 读取spi文件
     *
     * @param dir 文件目录
     */
    private void loadFile(String dir) {
        String fileName = dir + this.type.getName();

        try {
            ClassLoader classLoader = findClassLoader();
            Enumeration t;
            if (classLoader != null) {
                t = classLoader.getResources(fileName);
            } else {
                t = ClassLoader.getSystemResources(fileName);
            }

            if (t != null) {
                label269:
                while (t.hasMoreElements()) {
                    java.net.URL url = (java.net.URL) t.nextElement();

                    try {
                        BufferedReader t1 = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));

                        try {
                            String line = null;

                            while (true) {
                                do {
                                    if ((line = t1.readLine()) == null) {
                                        continue label269;
                                    }

                                    int ci = line.indexOf('#');
                                    if (ci >= 0) {
                                        line = line.substring(0, ci);
                                    }

                                    line = line.trim();
                                } while (line.length() <= 0);

                                try {
                                    String t2 = null;
                                    int var32 = line.indexOf('=');
                                    if (var32 > 0) {
                                        t2 = line.substring(0, var32).trim();
                                        line = line.substring(var32 + 1).trim();
                                    }

                                    if (line.length() > 0) {
                                        Class clazz = Class.forName(line, true, classLoader);
                                        if (!this.type.isAssignableFrom(clazz)) {
                                            throw new IllegalStateException("Error when load extension class(interface: "
                                                    + this.type + ", class line: " + clazz.getName() + "), class "
                                                    + clazz.getName() + "is not subtype of interface.");
                                        }
                                        extendClasses.putIfAbsent(t2, clazz);

                                    }
                                } catch (Throwable th) {
                                    //TODO: 异常处理
                                }
                            }
                        } finally {
                            t1.close();
                        }
                    } catch (Throwable th) {
                        //TODO: 异常处理
                    }
                }
            }
        } catch (Throwable th) {
            //TODO: 异常处理
        }

    }


}
