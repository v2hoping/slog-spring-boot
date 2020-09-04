# slog-spring-boot
可支持SQL执行日志获取，基于Springboot项目可自动集成，简单快速定位SQL问题

## 特征与作用

* **支持SpringMvc、Mybatis**——通过SpringMvc过滤器、Mybaits拦截器获得SQL语句、SQL参数、执行时间
* **支持Http Json**——对返回格式是Json形式的接口，自动添加slog字段，返回一个执行SQL集合
* **安全性配置**——支持不同环境不同模式，模式枚举：无密码模式、关闭、有密码模式
* **Spring Boot项目可快速集成**——通过@EnableSlog注解开启
