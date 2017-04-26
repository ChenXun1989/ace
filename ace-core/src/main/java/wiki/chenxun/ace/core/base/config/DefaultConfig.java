package wiki.chenxun.ace.core.base.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;
import wiki.chenxun.ace.core.base.common.AceApplicationConfig;
import wiki.chenxun.ace.core.base.common.AceServerConfig;
import wiki.chenxun.ace.core.base.register.RegisterConfig;
import wiki.chenxun.ace.core.base.support.InetAddressUtil;

import java.io.IOException;
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

    private volatile ZooKeeper zooKeeper;



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

    @Override
    public synchronized void export() {
        if (zooKeeper != null) {
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RegisterConfig registerConfig = (RegisterConfig) configInstances.get(RegisterConfig.class).getConfigBean();
                try {
                    zooKeeper = new ZooKeeper(registerConfig.getUrl(), registerConfig.getTimeout(), new Watcher() {
                        @Override
                        public void process(WatchedEvent watchedEvent) {

                        }
                    });
                    String ip = InetAddressUtil.getHostIp();
                    AceServerConfig aceServerConfig = (AceServerConfig) configBeanParser(AceServerConfig.class).getConfigBean();
                    String tmp = ip + ":" + aceServerConfig.getPort();
                    for (ConfigBeanParser parser : configInstances.values()) {
                        ConfigBean configBean = parser.getConfigBean().getClass().getAnnotation(ConfigBean.class);
                        String path = configBean.value();
                        path = ROOT_PATH  + path;
                        createNode(path);
                        ObjectMapper om = new ObjectMapper();
                        byte[] data = om.writer().writeValueAsBytes(parser.getConfigBean());
                        zooKeeper.create(path + "/" + tmp, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("ace-config");
        thread.setDaemon(true);
        thread.start();
    }


    private void createNode(String path) throws KeeperException, InterruptedException {
        String[] arr = path.split("/");
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null && arr[i].trim().length() > 0) {
                stringBuffer.append("/");
                stringBuffer.append(arr[i]);
                String node = stringBuffer.toString();
                Stat state = zooKeeper.exists(node, true);
                if (state == null) {
                    zooKeeper.create(node, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }

        }


    }

    public void clean(){
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

}
