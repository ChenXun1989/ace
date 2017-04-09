package wiki.chenxun.ace.examples.simple;

import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.annotations.Get;
import wiki.chenxun.ace.core.base.annotations.RequestParam;

/**
 * @Description: Created by chenxun on 2017/4/8.
 */
@AceService(path = "/person")
public class PersonServiceImpl implements PersonService {


    @Get
    @Override
    public Person test(@RequestParam String name, @RequestParam int age) {
        Person person = new Person();
        person.setAge(age);
        person.setName(name);
        return person;
    }
}
