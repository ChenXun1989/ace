package wiki.chenxun.ace.core.base.example;

import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.annotations.Get;

/**
 * @Description: Created by chenxun on 2017/4/8.
 */
@AceService(path = "/person")
public class PersonServiceImpl  implements  PersonService{


    @Get
    @Override
    public Person test() {
        Person person=new Person();
        person.setAge(22);
        person.setName("abc");
        return person;
    }
}
