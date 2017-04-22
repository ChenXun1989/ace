package wiki.chenxun.ace.test;

import wiki.chenxun.ace.core.base.common.AceApplication;
import wiki.chenxun.ace.core.base.common.AceApplicationConfig;
import wiki.chenxun.ace.core.base.common.AceServiceBean;

/**
 * @Description: Created by chenxun on 2017/4/21.
 */
public class TestDemoService {


    public static void main(String[] args) {

        AceApplication aceApplication = new AceApplication();
        AceApplicationConfig applicationConfig = new AceApplicationConfig();
        applicationConfig.setName("ace-test");
        aceApplication.setConfigBean(applicationConfig);
        DemoService demoService = new DemoServiceImpl();

        AceServiceBean<DemoService> aceServiceBean = new AceServiceBean<>();
        aceServiceBean.setInstance(demoService);
        aceServiceBean.setPath("/demo");

        aceApplication.register(aceServiceBean);
        aceApplication.start();
        aceApplication.close();


    }

}
