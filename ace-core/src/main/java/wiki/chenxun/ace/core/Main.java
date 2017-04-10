package wiki.chenxun.ace.core;

import wiki.chenxun.ace.core.base.common.Context;
import wiki.chenxun.ace.core.base.common.ExtendLoader;
import wiki.chenxun.ace.core.base.container.Container;


/**
 * 项目启动类
 * Created by chenxun on 2017/4/7.
 */
public final class Main {
    /**
     * 服务运行标示，0运行，其他停止
     */
    private static volatile int flag = 0;

    private Main() {

    }

    /**
     * 入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {

        ExtendLoader<Container> loader = ExtendLoader.getExtendLoader(Container.class);
        Container container = loader.getExtension(ExtendLoader.DEFAULT_SPI_NAME);
        Context.setCurrentContainer(container);
        container.init(args);
        container.registerShutdownHook();
        container.start();
        Context.notifyObservers(Context.Event.STARTED);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Context.notifyObservers(Context.Event.STOPED);
                synchronized (Main.class) {
                    flag = 1;
                }
            }
        }));


        synchronized (Main.class) {
            while (flag == 0) {
                try {
                    Main.class.wait();
                } catch (InterruptedException ignore) {

                }
            }
        }

        System.out.println("ace Server stop !!!");


    }

}
