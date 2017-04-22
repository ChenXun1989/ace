package wiki.chenxun.ace.core;

import wiki.chenxun.ace.core.base.common.AceApplication;

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

        AceApplication aceApplication = new AceApplication();
        aceApplication.scan();
        aceApplication.start();
        aceApplication.close();


    }


}
