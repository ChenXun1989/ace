package wiki.chenxun.ace.core.base.config;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: Created by chenxun on 2017/4/16.
 */
public enum DefaultConfig implements Config {

    INSTANCE;

    /**
     * 配置类解析实例
     */
    private Map<Class, ConfigBeanParser> configInstances = new ConcurrentHashMap<>();


    @Override
    public ConfigBeanParser configBeanParser(Class cls) {
        ConfigBeanParser configBeanParser = configInstances.get(cls);
        if (configBeanParser == null) {
            configBeanParser = new ConfigBeanParser();
            configBeanParser.parser(cls);
            add(configBeanParser);
        }
        return configBeanParser;

    }

    @Override
    public void add(ConfigBeanParser parser) {
        configInstances.put(parser.getConfigBean().getClass(), parser);
    }
}
