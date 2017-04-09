package wiki.chenxun.ace.examples.simple;

import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.annotations.Get;

/**
 * @Description: Created by chenxun on 2017/4/8.
 */
@AceService(path = "/order")
public class OrderServiceImpl implements  OrderService {

    @Get
    @Override
    public String test() {
        return "order";
    }
}
