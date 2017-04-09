package wiki.chenxun.ace.core.base.container;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import wiki.chenxun.ace.core.base.annotations.RequestParam;
import wiki.chenxun.ace.core.base.support.ReflectUtil;
import wiki.chenxun.ace.core.base.support.SpringBeanUtil;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.annotations.Get;
import wiki.chenxun.ace.core.base.annotations.Post;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: 核心类，单例
 * Created by chenxun on 2017/4/8.
 */
@Component
public final class Dispatcher implements ApplicationListener<ContextRefreshedEvent> {


    /**
     * spring容器上下文
     */
    private ApplicationContext applicationContext;

    /**
     * aceServiceMap实例集合
     */
    private final Map<String, Object> aceServiceMap = new HashMap<>();
    /**
     * uri-path 与 method的映射
     */
    private final Map<String, MethodDefine> pathMap = new HashMap<>();


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
        //路由方法
        HttpMethod httpMethod = null;
        String path = request.uri();
        String getParamers = null;
        if (path.contains("?")) {
            getParamers = path.substring(path.indexOf("?") + 1);
            path = path.substring(0, path.indexOf("?"));
        }
        // 不带请求方式前缀
        Object obj = aceServiceMap.get(path);

        if (HttpMethod.GET.equals(request.method())) {
            httpMethod = httpMethod.GET;
            path = MethodDefine.GET + path;
        } else if (HttpMethod.POST.equals(request.method())) {
            httpMethod = httpMethod.POST;
            path = MethodDefine.POST + path;
        }

        MethodDefine methodDefine = pathMap.get(path);

        Object result = null;
        // TODO:参数转换
        List args = new ArrayList();
        if (!CollectionUtils.isEmpty(methodDefine.getParamKeyList())) {
            // get参数转换
            if (getParamers != null && getParamers.contains("&")) {
                String[] arr = getParamers.split("&");
                for (int i = 0; i < methodDefine.getParamKeyList().size(); i++) {
                    String key = methodDefine.getParamKeyList().get(i);
                    for (String str : arr) {
                        if (str.contains("=")) {
                            String k = str.split("=")[0];
                            if (k.equals(key)) {
                                Class cls = methodDefine.getParamClassList().get(i);
                                String v = str.split("=")[1];
                                // 基本类型和string 支持
                                if (String.class.equals(cls)) {
                                    v = v.trim();
                                    args.add(v);
                                } else if (int.class.equals(cls)) {
                                    args.add(Integer.valueOf(v));
                                }

                            }
                        }
                    }
                }
            }
        }

        //调用方法
        result = methodDefine.getMethod().invoke(obj, args.isEmpty() ? null : args.toArray(new Object[args.size()]));


        return result;
    }

    /**
     * spring容器启动完毕之后触发
     *
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
                MethodDefine methodDefine = new MethodDefine();
                Method[] methods = cls.getMethods();
                for (Method m : methods) {
                    String path = null;
                    if (m.getAnnotation(Get.class) != null) {
                        path = MethodDefine.GET + aceService.path();
                    } else if (m.getAnnotation(Post.class) != null) {
                        path = MethodDefine.POST + aceService.path();
                    }
                    if (pathMap.containsKey(path)) {
                        // TODO：重复的path 异常

                    } else {
                        methodDefine.setPath(path);
                        methodDefine.setMethod(m);
                        // 参数解析
                        Parameter[] parameters = m.getParameters();
                        if (parameters != null || parameters.length > 0) {
                            Map<String, Class> params = new TreeMap<>();
                            try {
                                List<String> paramKeyList = new ArrayList<>();
                                List<Class> paramClassList = new ArrayList<>();
                                String[] names = ReflectUtil.getMethodParamNames(m);
                                for (int i = 0; i < parameters.length; i++) {
                                    Parameter parameter = parameters[i];
                                    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                                    if (requestParam != null) {

                                        String key = requestParam.value();
                                        if (StringUtils.isEmpty(key)) {
                                            key = names[i];
                                        }
                                        if (paramKeyList.contains(key)) {
                                            // TODO: 参数重复异常
                                        } else {
                                            paramKeyList.add(key);
                                            paramClassList.add(parameter.getType());
                                        }
                                    }
                                }
                                methodDefine.setParamKeyList(paramKeyList);
                                methodDefine.setParamClassList(paramClassList);

                            } catch (IOException e) {
                                // TODO: 异常处理
                            }
                            pathMap.put(path, methodDefine);


                        }
                        pathMap.put(path, methodDefine);
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
