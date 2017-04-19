package wiki.chenxun.ace.core.base.container;

import lombok.Data;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;

/**
 * @Description: Created by chenxun on 2017/4/18.
 */
@Data
@ConfigBean(ApplicationProperties.PREFIX)
public class ApplicationProperties {

    public static final String PREFIX="application";

    private String container;

    private String server;

    private String register;

}
