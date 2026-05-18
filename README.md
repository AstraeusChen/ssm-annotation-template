一个练手项目
SSM全注解前后端分离用户管理系统
*Spring6 / MyBatis / Tomcat10 / JDK17*

基于原生 Spring6+SpringMVC+MyBatis 全注解开发，弃用 web.xml，实现 RESTful 风格接口的前后端分离项目。

使用 Jakarta EE 规范，适配 JDK17 模块化，通过 AbstractAnnotationConfigDispatcherServletInitializer 启动容器。

设计统一响应体、全局异常处理、DTO/VO 入出参分离，采用 Spring-Crypto 实现 BCrypt 密码加密，遵守阿里巴巴开发规范。

集成 PageHelper 分页、Jakarta Validation 分组校验、SpringDoc 接口文档，提升开发效率。

自定义 @WebLog 注解+AOP 实现接口日志收集，MDC+拦截器实现全链路 TraceId 追踪，方便问题定位。

使用 @Profile 实现多环境配置隔离，编写 JUnit5 单元测试覆盖核心业务，保证代码质量。

前端使用原生 HTML+Axios 联调，加深对前后端交互的理解。
