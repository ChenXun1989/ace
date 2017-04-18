package wiki.chenxun.ace.core.base.test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import javax.inject.Inject;
import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @Description: Created by chenxun on 2017/4/12.
 */
public class AceJUnit4ClassRunner extends BlockJUnit4ClassRunner {

    public AceJUnit4ClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }


    @Override
    protected Object createTest() throws Exception {
        Object obj = super.createTest();
        Class cls = obj.getClass();


        Field[] fields = cls.getFields();
        if (fields != null) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    //TODO：注入服务
                }
            }
        }


        return obj;
    }
}
