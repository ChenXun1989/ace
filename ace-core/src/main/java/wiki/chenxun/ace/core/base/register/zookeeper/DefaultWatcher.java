package wiki.chenxun.ace.core.base.register.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import wiki.chenxun.ace.core.base.common.ExtendLoader;

import java.util.Observable;
import java.util.Observer;

/**
 * @Description: Created by chenxun on 2017/4/12.
 */
public class DefaultWatcher extends Observable implements Watcher  {


    public DefaultWatcher(){
       init();
    }

    private void init(){
        // TODO: 绑定观察者

    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        EventType eventType= watchedEvent.getType();
        switch (eventType){
            case NodeDeleted:
                System.out.println("create");
             default:

        }


        setChanged();
        notifyObservers();




    }


}
