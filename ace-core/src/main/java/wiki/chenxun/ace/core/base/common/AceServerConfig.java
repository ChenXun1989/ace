package wiki.chenxun.ace.core.base.common;

import lombok.Data;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;

/**
 * @Description: Created by chenxun on 2017/4/22.
 */
@Data
@ConfigBean(AceServerConfig.PREFIX)
public class AceServerConfig {

    public static final String PREFIX = "ace.server";

    private long timeout = 1000L;

    private int port = 8080;

    private int backSize = 1024;
}
