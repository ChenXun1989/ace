package wiki.chenxun.ace.core.base.remote.netty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import wiki.chenxun.ace.core.base.common.ApplicationInfo;
import wiki.chenxun.ace.core.base.remote.Dispatcher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


/**
 * @Description: 核心类，单例
 * Created by chenxun on 2017/4/8.
 */
public class DefaultDispatcher implements Dispatcher {


    /**
     * 请求分发与处理
     *
     * @param request http协议请求
     * @return 处理结果
     * @throws InvocationTargetException 调用异常
     * @throws IllegalAccessException    参数异常
     */
    @SuppressWarnings("unchecked")
    public Object doDispatcher(FullHttpRequest request) throws InvocationTargetException, IllegalAccessException {

        if (request.method().equals(HttpMethod.GET)) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            System.out.println(decoder.parameters());
        } else if (request.method().equals(HttpMethod.POST)) {
            //如果要多次解析，请用 request.content().copy()
            String jsonStr = request.content().toString(CharsetUtil.UTF_8);
            ObjectMapper om = new ObjectMapper();
            try {
                JsonNode node = om.reader().readTree(jsonStr);
                System.out.println(node);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        // TODO:路由方法

        // TODO:调用方法

        ApplicationInfo mock = new ApplicationInfo();
        mock.setName("ace");
        mock.setVersion("1.0");
        mock.setDesc(" mock  !!! ");
        return mock;
    }


}
