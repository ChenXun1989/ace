package wiki.chenxun.ace.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * @Description: Created by chenxun on 2017/4/7.
 */
public class Main {

    public static void main(String[] args) {
        String basePackage="wiki.chenxun.ace.core";
        AnnotationConfigApplicationContext applicationContext=new AnnotationConfigApplicationContext(basePackage);
        applicationContext.registerShutdownHook();
        applicationContext.start();


    }

}
