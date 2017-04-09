package wiki.chenxun.ace.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * 项目启动类
 * Created by chenxun on 2017/4/7.
 */
public final class Main {

    private Main() {

    }

    /**
     * 入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        String basePackage = "wiki.chenxun.ace.core";
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(basePackage);
        applicationContext.registerShutdownHook();
        applicationContext.start();


    }

}
