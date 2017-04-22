package wiki.chenxun.ace.core.base.config;

import wiki.chenxun.ace.core.base.annotations.ConfigBean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.MissingResourceException;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * @Description: Created by chenxun on 2017/4/22.
 */
public class ConfigBeanParser<T> extends Observable {

    private volatile T instance;

    public T getConfigBean() {
        return instance;
    }


    public void parser(Class<T> t) {
        ResourceBundle resource = ResourceBundle.getBundle(Config.DEFAULT_PATH);
        ConfigBean configBean = (ConfigBean) t.getAnnotation(ConfigBean.class);
        String prefix = configBean.value();
        BeanInfo beanInfo = null;
        try {
            instance = t.newInstance();
            beanInfo = Introspector.getBeanInfo(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //MissingResourceException

        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String key = prefix + "." + propertyDescriptor.getName();
            Method method = propertyDescriptor.getWriteMethod();
            if (method != null) {
                try {
                    String value = resource.getString(key);
                    Class paramClass = method.getParameterTypes()[0];
                    method.invoke(instance, parse(paramClass, value));
                } catch (MissingResourceException e) {
                    //TODO： 记录日志
                } catch (IllegalAccessException e) {
                    //TODO： 记录日志
                } catch (InvocationTargetException e) {
                    //TODO： 记录日志
                }
            }

        }
    }


    /**
     * parse
     *
     * @param cls   Class
     * @param value String
     * @return ObjectObject
     */
    private Object parse(Class cls, String value) {
        if (int.class.equals(cls)) {
            return Integer.parseInt(value);
        } else if (long.class.equals(cls)) {
            return Long.parseLong(value);
        } else if (double.class.equals(cls)) {
            return Double.parseDouble(value);
        } else if (float.class.equals(cls)) {
            return Float.parseFloat(value);
        } else if (byte.class.equals(cls)) {
            return Byte.parseByte(value);
        }
        return value;

    }


}
