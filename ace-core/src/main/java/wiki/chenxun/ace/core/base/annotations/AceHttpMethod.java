package wiki.chenxun.ace.core.base.annotations;

import java.lang.annotation.Annotation;

/**
 * httpmethod
 * Created by Z800 on 2017/4/11.
 */
public enum AceHttpMethod {
    /**
     * get
     */
    GET,
    /**
     * post
     */
    POST,
    /**
     * put
     */
    PUT,
    /**
     * delete
     */
    DELETE;

    /**
     * 获取对应的注解类型
     * @return 注解类型
     */
    public Class<? extends Annotation> getAnnotationClazz() {
        if (AceHttpMethod.GET.equals(this)) {
            return Get.class;
        } else if (AceHttpMethod.POST.equals(this)) {
            return Post.class;
        } else if (AceHttpMethod.PUT.equals(this)) {
            return Put.class;
        } else if (AceHttpMethod.DELETE.equals(this)) {
            return Delete.class;
        } else {
            return null;
        }
    }

}
