package wiki.chenxun.ace.test;

import org.springframework.aop.framework.AopContext;

import java.time.LocalDateTime;

/**
 * @Description: Created by chenxun on 2017/4/11.
 */
public class TestChar {

    public static void main(String[] args) {
        System.out.println(TestChar.class.getClass());
        System.out.println(TestChar.class);



        LocalDateTime now=LocalDateTime.now();
        LocalDateTime dateTime= LocalDateTime.of(2017,4,19,6,6);

        System.out.println(now);
        System.out.println(dateTime);

        System.out.println(now.toLocalDate().equals(dateTime.toLocalDate()));


        char a=(char) 35;
        System.out.println(a);
        a=(char) 61;
        System.out.println(a);

    }
}
