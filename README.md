# ace
soa  framework
## 快速开始
+ 启动：运行java类 wiki.chenxun.ace.core.Main的main方法
+ 测试：通过postman或者curl类测试
+ 自带的demo在 wiki.chenxun.ace.examples.simple.test 包下面
+ 现有测试链接：http://localhost:8080/person?name=abc&age=224
+ jdk版本 1.8 ，如需降级，请修改项目根路径下的pom.xml 里面有指定jdk版本。至少1.7
+ 测试

## 加入我们
qq群：317059140 问题答案：576481228


##服务启动
1. Application applicaiton=new Application
2. applicaiton.initConfig();
3. Container cotainer=applicaiton.getContainer();
4. XXXService xxx=Container.getBean(xxx);
5. AceService<XXX> aceService=new AceServcie();
6. aceService.initConfig();
7. application.register(aceService);
8. application.start(); 

##领域模型
###Application
> 表示一个应用。对应一个 applicationConfig 属性有logger。server，生命周期与进程同在

###AceService
> 表示一个服务，对应一个ServcieConfig 属性有path，生命周期小于进程

*application与aceService 存在有属性，例如timeout*
### Container
> 表示一个ioc 容器，管理普通bean

### server
> 表示一个socket服务，生命周期由application管理。
##调用过程
 curl http -> server-> AceService-> ioc Bean 


