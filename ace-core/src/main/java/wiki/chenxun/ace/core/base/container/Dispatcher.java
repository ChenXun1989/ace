package wiki.chenxun.ace.core.base.container;

import io.netty.handler.codec.http.*;
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


    private ApplicationContext applicationContext;

    private final Map<String, Object> aceServiceMap= new HashMap<>();

    private final Map<String,Method>  pathMap=new HashMap<>();


    @SuppressWarnings("unchecked")
    public Object doDispatcher(FullHttpRequest request) throws InvocationTargetException, IllegalAccessException {
        //路由方法
        String path=request.uri();
        if(path.contains("?")){
           path= path.substring(0,path.indexOf("?"));
        }
        // 不带请求方式前缀
        Object obj=aceServiceMap.get(path);

        if(HttpMethod.GET.equals(request.method())){
            path="_get"+path;
        }else if(HttpMethod.POST.equals(request.method())){
            path="_post"+path;
        }

        Method method=pathMap.get(path);

        // TODO:参数转换

        //调用方法
        Object result=method.invoke(obj,null);

        return result;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        applicationContext=contextRefreshedEvent.getApplicationContext();
        Map<String, Object> map=applicationContext.getBeansWithAnnotation(AceService.class);
        if(CollectionUtils.isEmpty(map)){
            // warn
        }else {
            for(Map.Entry<String,Object> entry : map.entrySet()){
                Class cls=entry.getValue().getClass();
                AceService aceService= (AceService) cls.getAnnotation(AceService.class);
                if(aceServiceMap.containsKey(aceService.path())){
                    //关闭容器
                    SpringBeanUtil.closeContext();
                }else {
                    aceServiceMap.put(aceService.path(),entry.getValue());
                }
                Method[] methods=cls.getMethods();
                for(Method m : methods){
                    String path=null;
                    if(m.getAnnotation(Get.class)!=null){
                        path ="_get"+aceService.path();
                    }
                    else if(m.getAnnotation(Post.class)!=null){
                        path ="_post"+aceService.path();
                    }
                    if(pathMap.containsKey(path)){
                        // 重复的path 异常

                    }else {
                        pathMap.put(path,m);
                    }
                }


            }
        }


        Server server=applicationContext.getBean(Server.class);
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
