package wiki.chenxun.ace.core.base.remote;

import lombok.Data;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;

/**
 * @Description: Created by chenxun on 2017/4/16.
 */
@Data
@ConfigBean(ServerProperties.PREFIX)
public class ServerProperties {

    public static final String  PREFIX="server";

    private int port =8080;

    private String dispatch;

    private int backSize=1024;




}
