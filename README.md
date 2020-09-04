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

#### SpringMvc、Mybatis环境

```xml
<dependency>
    <groupId>com.v2hoping</groupId>
    <artifactId>slog</artifactId>
    <version>1.1.0</version>
</dependency>
```

#### Spring Boot环境


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
|---|:---:|---|
| slog.active  | true/false | 是否启用，默认不启动 |
| slog.passwordActive | true/false  |  是否密码模式，默认否  |
| slog.password  | 字符串 | 密码，为空则不使用密码 |

密码模式
------------
密码模式必须通过请求头传递密码
| 请求头  | 类型 | 说明  |
|---|:---:|---|
| Slog-Pwd  | 字符串 | 传递配置的密码 |


示例
------------

#### 第一步：确认环境

确认Maven已经引入SpringMvc、Mybatis、Springboot

#### 第二步：Maven引入jar

```xml
<dependencies>
   <dependency>
      <groupId>com.v2hoping</groupId>
      <artifactId>slog-spring-boot-starter</artifactId>
      <version>1.1.0</version>
   </dependency>
</dependencies>
```

#### 第三步：环境配置

建议测试环境开启，正式环境可以关闭或者选择密码模式开启。

```java
@SpringBootApplication
@EnableSlog
public class ServletInitializer extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ServletInitializer.class);
        Map<String, Object> defaultProperties = new HashMap<>(1);
        defaultProperties.put("druid.datasource.primary.base.package");
        defaultProperties.put("registerErrorPageFilter",false);
        System.setProperty("spring.devtools.restart.enabled", "false");
        application.setDefaultProperties(defaultProperties);
        application.setWebEnvironment(true);
        application.run(args);
    }
}
```

无密码模式：
```
slog.active=true
```

密码模式：
```
slog.active=true
slog.passwordActive=true
slog.password=123456
```

#### 第四步：http请求返回
不添加slog组件时
```json
{
    "codeNum": 0,
    "success": true,
    "value": {
        "rr": [
            {
                "name": "医疗健康",
                "rank": 1
            },
            {
                "name": "企业服务",
                "rank": 2
            },
            {
                "name": "金融",
                "rank": 3
            }
        ],
        "content": "一季度医疗健康、企业服务、金融"
    }
}
```
添加slog组件后
```json
{
	"codeNum": 0,
	"success": true,
	"slog": [{
			"param": "",
			"sql": "select element_type_id,element_type from ad_ver",
			"time": 3
		},
		{
			"param": "701001(Integer), 1(Integer), 2019(Integer)",
			"sql": "select financing_amount_rank from ad_in where platform_id=? and date_id = ? and year = ?",
			"time": 3
		}
	],
	"value": {
		"rr": [{
				"name": "医疗健康",
				"rank": 1
			},
			{
				"name": "企业服务",
				"rank": 2
			},
			{
				"name": "金融",
				"rank": 3
			}
		],
		"content": "一季度医疗健康、企业服务、金融"
	}
}
```











