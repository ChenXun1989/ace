package wiki.chenxun.ace.core.base.container;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import wiki.chenxun.ace.core.base.support.SpringBeanUtil;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.annotations.Get;
import wiki.chenxun.ace.core.base.annotations.Post;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 核心类，单例
 * Created by chenxun on 2017/4/8.
 */
@Component
public final class Dispatcher implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * get请求前缀
     */
    public static final String GET = "_get";
    /**
     * post请求前缀
     */
    public static final String POST = "_post";
    /**
     * spring容器上下文
     */
    private ApplicationContext applicationContext;

    /**
     * aceServiceMap实例集合
     */
    private final Map<String, Object> aceServiceMap = new HashMap<>();
    /**
     *  uri-path 与 method的映射
     */
    private final Map<String, Method> pathMap = new HashMap<>();

    /**
     *  请求分发与处理
     * @param request http协议请求
     * @return 处理结果
     * @throws InvocationTargetException 调用异常
     * @throws IllegalAccessException 参数异常
     */
    @SuppressWarnings("unchecked")
    public Object doDispatcher(FullHttpRequest request) throws InvocationTargetException, IllegalAccessException {
        //路由方法
        String path = request.uri();
        if (path.contains("?")) {
            path = path.substring(0, path.indexOf("?"));
        }
        // 不带请求方式前缀
        Object obj = aceServiceMap.get(path);

        if (HttpMethod.GET.equals(request.method())) {
            path = GET + path;
        } else if (HttpMethod.POST.equals(request.method())) {
            path = POST + path;
        }

        Method method = pathMap.get(path);

        // TODO:参数转换

        //调用方法
        Object result = method.invoke(obj, null);

        return result;
    }

    /**
     *  spring容器启动完毕之后触发
     * @param contextRefreshedEvent
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(AceService.class);
        if (CollectionUtils.isEmpty(map)) {
            // TODO: warn
            System.out.println("warn  ....");
        } else {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Class cls = entry.getValue().getClass();
                AceService aceService = (AceService) cls.getAnnotation(AceService.class);
                if (aceServiceMap.containsKey(aceService.path())) {
                    //关闭容器
                    SpringBeanUtil.closeContext();
                } else {
                    aceServiceMap.put(aceService.path(), entry.getValue());
                }
                Method[] methods = cls.getMethods();
                for (Method m : methods) {
                    String path = null;
                    if (m.getAnnotation(Get.class) != null) {
                        path = GET + aceService.path();
                    } else if (m.getAnnotation(Post.class) != null) {
                        path = POST + aceService.path();
                    }
                    if (pathMap.containsKey(path)) {
                        // 重复的path 异常

                    } else {
                        pathMap.put(path, m);
                    }
                }


            }
        }


        final Server server = applicationContext.getBean(Server.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                } catch (Exception e) {
                    // TODO: 异常处理
                }
            }
        }).start();

        System.out.println("ace server stater !!!");


    }
}
