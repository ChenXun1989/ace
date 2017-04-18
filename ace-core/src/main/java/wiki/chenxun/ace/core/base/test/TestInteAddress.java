package wiki.chenxun.ace.core.base.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/12.
 */
public class TestInteAddress {

    private TestInteAddress() {
    }

    public static void main(String[] args) throws UnknownHostException {
        List<Long> list=new ArrayList<>();
        list.add(66L);
        list.add(77L);
        String str=String.format("abc %s" ,list);
        System.out.println(str);

        InetAddress inetAddress = InetAddress.getLocalHost();
        String canonical = inetAddress.getCanonicalHostName();
        String host = inetAddress.getHostName();

        System.out.println(canonical);
        System.out.println(host);

    }
}
