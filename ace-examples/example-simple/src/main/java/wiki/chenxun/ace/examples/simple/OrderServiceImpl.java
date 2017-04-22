package wiki.chenxun.ace.examples.simple;

import org.springframework.stereotype.Component;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.annotations.Get;

/**
 * @Description: Created by chenxun on 2017/4/8.
 */
@AceService(path = "/order")
@Component
public class OrderServiceImpl implements  OrderService {

    @Get
    @Override
    public String test() {
        return "order";
    }
}
