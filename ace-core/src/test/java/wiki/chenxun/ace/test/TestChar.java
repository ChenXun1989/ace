package wiki.chenxun.ace.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import org.springframework.aop.framework.AopContext;
import wiki.chenxun.ace.core.base.common.AceApplicationConfig;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * @Description: Created by chenxun on 2017/4/11.
 */
public class TestChar {

    public static void main(String[] args) throws IOException {

        AceApplicationConfig aceApplicationConfig=new AceApplicationConfig();

        ObjectMapper objectMapper=new ObjectMapper();
        byte[] bytes=objectMapper.writeValueAsBytes(aceApplicationConfig);
        //BeanSerializer jsonSerializer=new BeanSerializer();
        aceApplicationConfig= objectMapper.readValue(bytes,aceApplicationConfig.getClass());
        System.out.println(aceApplicationConfig.getPackages());
        Pattern p = Pattern.compile(".?.html|.jpg|.png|.css|.js");
        System.out.println(p.matcher("a.html").find());
        System.out.println(p.matcher("a.htm").find());
        System.out.println(p.matcher("a.css").find());
        System.out.println(p.matcher("a.js").find());
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
