package wiki.chenxun.ace.core.base.support;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description: Created by chenxun on 2017/4/23.
 */
public class InetAddressUtil {

    public static String getHostIp() {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
