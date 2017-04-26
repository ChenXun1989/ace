package wiki.chenxun.ace.core.base.register.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import wiki.chenxun.ace.core.base.register.RegisterConfig;


/**
 * @Description: Created by chenxun on 2017/4/12.
 */
public class DefaultWatcher implements Watcher {

    private RegisterConfig registerConfig;

    public DefaultWatcher(RegisterConfig registerConfig) {
        this.registerConfig = registerConfig;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        EventType eventType = watchedEvent.getType();
        switch (eventType) {
            case NodeDeleted:
                System.out.println("create");
            default:

        }
        // TODO: 更新配置

    }


}
