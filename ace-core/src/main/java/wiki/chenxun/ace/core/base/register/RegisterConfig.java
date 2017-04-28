package wiki.chenxun.ace.core.base.register;

import lombok.Data;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;

/**
 * @Description: Created by chenxun on 2017/4/20.
 */
@Data
@ConfigBean(RegisterConfig.PREFIX)
public class RegisterConfig {
    /**
     * PREFIX
     */
    public static final  String PREFIX="ace.register";
    /**
     * url
     */
    private String url="localhost:2181";

    private int timeout=30000;



}
