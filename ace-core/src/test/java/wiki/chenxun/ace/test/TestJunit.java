package wiki.chenxun.ace.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import wiki.chenxun.ace.core.base.test.AceJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * @Description: Created by chenxun on 2017/4/12.
 */

@RunWith(AceJUnit4ClassRunner.class)
public class TestJunit {
    @Inject
    private DemoService demoService;

    @Test
    public void test_01(){
        demoService.say();
    }
}
