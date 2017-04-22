package wiki.chenxun.ace.examples.simple;

import lombok.Data;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;

/**
 * @Description: Created by chenxun on 2017/4/17.
 */
@Data
@ConfigBean(PersonProperties.PREFIX)
public class PersonProperties {

    public static final String PREFIX="person";

    private String name;

    private int age;


}
