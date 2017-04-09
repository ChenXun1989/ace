package wiki.chenxun.ace.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目启动类
 * Created by chenxun on 2017/4/7.
 */
public final class Main {
    /**
     * aceServicePackage
     */
    public static final String ACE_SERVICE_PACKAGE = "aceServicePackage:";

    private Main() {

    }

    /**
     * 入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        List<String> scanPackageList = new ArrayList<>();
        scanPackageList.add("wiki.chenxun.ace.core");
        if (null != args && args.length > 0) {
            for (String arg : args) {
                if (arg.startsWith(ACE_SERVICE_PACKAGE)) {
                    scanPackageList.add(arg.substring(arg.indexOf(ACE_SERVICE_PACKAGE) + ACE_SERVICE_PACKAGE.length()));
                }
            }
        }
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(scanPackageList.toArray(new String[scanPackageList.size()]));
        applicationContext.registerShutdownHook();
        applicationContext.start();


    }

}
