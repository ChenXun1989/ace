package wiki.chenxun.ace.core.base.register.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import wiki.chenxun.ace.core.base.config.Config;
import wiki.chenxun.ace.core.base.register.Register;
import wiki.chenxun.ace.core.base.register.RegisterConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Observable;

/**
 * @Description: Created by chenxun on 2017/4/12.
 */
public class ZookeeperRegister implements Register {

    private ZooKeeper zooKeeper;

    private RegisterConfig registerConfig;

    public void init() throws IOException {

        String url = "localhost:2080";
        zooKeeper = new ZooKeeper(url, 60 * 100, new DefaultWatcher(registerConfig));
        createRootNode();

    }


    private void createRootNode() {
        try {
            Stat stat = zooKeeper.exists(ROOT, false);
            if (stat == null) {
                Date now = new Date();
                zooKeeper.create(ROOT, now.toString().getBytes(Charset.forName("UTF-8")), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void register() {


    }


    @Override
    public void unregister() {

    }


    @Override
    public void setConfigBean(RegisterConfig registerConfig) {
        this.registerConfig = registerConfig;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
