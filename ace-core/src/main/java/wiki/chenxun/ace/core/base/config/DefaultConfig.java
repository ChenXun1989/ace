package wiki.chenxun.ace.core.base.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description: Created by chenxun on 2017/4/16.
 */
public class DefaultConfig implements Config {

    private Map<Class, Object> configInstances = new HashMap<>();

    @Override
    public <T> T configBean(Class<T> cls) {
        return (T) configInstances.get(cls);
    }

    @Override
    public <T> void add(T t) {
        configInstances.put(t.getClass(), t);
    }

    @Override
    public <T> void update(T t) {
        // TODO: 写入文件
        Properties props = new Properties();
        try {
            props.load(Config.class.getClassLoader().getResourceAsStream(Config.DEFAULT_PATH+".properties"));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
