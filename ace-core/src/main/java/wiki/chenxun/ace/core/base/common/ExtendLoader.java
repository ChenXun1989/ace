package wiki.chenxun.ace.core.base.common;

import wiki.chenxun.ace.core.base.annotations.Spi;
import wiki.chenxun.ace.core.base.container.Container;
import wiki.chenxun.ace.core.base.exception.ExtendLoadException;
import wiki.chenxun.ace.core.base.remote.Server;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 扩展点加载类
 *
 * @param <T> spi扩展点
 */
public final class ExtendLoader<T> implements Observer {

    /**
     * spi 默认值
     */
    public static final String DEFAULT_SPI_NAME = "default";
    /**
     * spi 文件位置
     */
    private static final String ACE_DIRECTORY = "META-INF/ace/";
    /**
     * 扩展类加载全局容器
     */
    private static final ConcurrentMap<Class<?>, ExtendLoader<?>> EXTEND_LOADERS = new ConcurrentHashMap();
    /**
     * 扩展点多个扩展实例容器
     */
    private final ConcurrentHashMap<String, Object> extendInstances = new ConcurrentHashMap<>();
    /**
     * 一个扩展点对应的多个扩展class
     */
    private final ConcurrentHashMap<String, Class> extendClasses = new ConcurrentHashMap<>();
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
        ExtendLoader loader = EXTEND_LOADERS.get(type);
        if (loader == null) {
            EXTEND_LOADERS.putIfAbsent(type, new ExtendLoader(type));
            loader = (ExtendLoader) EXTEND_LOADERS.get(type);
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
        if (name == null && name.trim().length() == 0) {
            throw new IllegalArgumentException("name  must not blank ");
        }
        if (cachedDefaultName == null) {
            this.loadExtensionClasses();
        }
        if (DEFAULT_SPI_NAME.equals(name)) {
            name = cachedDefaultName;
        }
        T t = (T) extendInstances.get(name);
        if (t == null) {
            Class cls = extendClasses.get(name);
            extendInstances.putIfAbsent(name, createExtension(name));
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
            injectProperty(t);
            return t;
        } catch (Exception e) {
            throw new ExtendLoadException("extend instance fail  ", e);
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
        Container container = Context.getCurrentContainer();
        BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        if (pds != null && pds.length > 0) {
            for (PropertyDescriptor pd : pds) {
                Method method = pd.getWriteMethod();
                if (method == null) {
                    continue;
                }
                Object obj = container.getBean(pd.getPropertyType(), pd.getName());
                method.invoke(t, obj);
            }
        }

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
                                } catch (Throwable var28) {
                                    IllegalStateException e = new IllegalStateException("Failed to load extension class(interface: "
                                            + this.type + ", class line: " + line + ") in "
                                            + url + ", cause: " + var28.getMessage(), var28);

                                }
                            }
                        } finally {
                            t1.close();
                        }
                    } catch (Throwable var30) {

                    }
                }
            }
        } catch (Throwable var31) {

        }

    }

    @Override
    public void update(Observable o, Object arg) {
        final Server server = ExtendLoader.getExtendLoader(Server.class).getExtension(DEFAULT_SPI_NAME);
        if (arg.equals(Context.Event.STARTED)) {
            //解析方法，暴露ace服务。
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        server.start();

                    } catch (Exception e) {
                        // TODO: 异常处理
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        } else if (arg.equals(Context.Event.STOPED)) {
            try {
                server.close();
            } catch (IOException e) {
                // 异常处理
            }
        }
    }
}
