slog-spring-boot
------------
可支持SQL执行日志获取，基于Springboot项目可自动集成，简单快速定位SQL问题

特征
------------

* **支持SpringMvc、Mybatis环境** — 通过SpringMvc过滤器、Mybaits拦截器获得SQL语句、SQL参数、执行时间
* **支持Http返回Json** — 对返回格式是Json形式的接口，自动添加slog字段，返回执行SQL集合
* **安全性配置** — 支持不同环境不同模式，模式枚举：无密码模式、关闭、有密码模式
* **Spring Boot项目可快速集成** — 通过@EnableSlog注解开启

Maven Setting
------------

### SpringMvc、Mybatis环境

```xml
<dependency>
    <groupId>com.v2hoping</groupId>
    <artifactId>slog</artifactId>
    <version>1.1.0</version>
</dependency>
```

### Spring Boot环境


```xml
<dependency>
    <groupId>com.v2hoping</groupId>
    <artifactId>slog-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```

配置
------------

| 名称   | 类型 | 说明  |
|---|:---:|---|---|
| slog.active  | true/false | 是否启用，默认不启动 |
| slog.passwordActive | true/false  |  是否密码模式，默认否  |
| slog.password  | 字符串 | 密码，为空则不使用密码 |





