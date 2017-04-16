package wiki.chenxun.ace.mybatis.test;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Description: Created by chenxun on 2017/4/15.
 */
@Data
@Table(name="t_person")
public class Person{

    @Id
    private long id;

    @Column(name="name_1")
    private String name;


}
