package wiki.chenxun.ace.core.base.remote;

import lombok.Data;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;

/**
 * @Description: Created by chenxun on 2017/4/16.
 */
@Data
@ConfigBean(ServerConfig.PREFIX)
public class ServerConfig {

    public static final String  PREFIX="server";

    private int port;




}
