package wiki.chenxun.ace.core.base.annotations;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description:
 * Created by chenxun on 2017/4/7.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface AceService {
    String value() default "";
    String path();
}
