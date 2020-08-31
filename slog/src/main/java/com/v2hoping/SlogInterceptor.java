package com.v2hoping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.jdbc.PreparedStatementLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * Created by houping wang on 2020/8/20
 *
 * @author houping wang
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class SlogInterceptor implements Interceptor, Filter {

    private SlogProperties slogProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(SlogInterceptor.class);

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String PWD_HEADER = "Slog-Pwd";

    public SlogInterceptor(SlogProperties slogProperties) {
        this.slogProperties = slogProperties;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        if(!(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }
        if(!this.checkProperty()) {
            chain.doFilter(request, response);
            return;
        }
        //启用密码验证
        if(this.slogProperties.getPasswordActive()) {
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            String accessPwd = this.slogProperties.getPassword();
            String pwd = httpServletRequest.getHeader(PWD_HEADER);
            if(!accessPwd.equals(pwd)) {
                chain.doFilter(request, response);
                return;
            }
        }
        //初始化线程变量
        SlogContext.init();
        //包装响应
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        FilterResponseWrapper filterResponseWrapper = new FilterResponseWrapper(httpServletResponse);
        //执行
        chain.doFilter(request, filterResponseWrapper);
        try{
            //检查仅允许返回值为application/json、可序列化为JSONObject新增该日志
            String contentType = filterResponseWrapper.getContentType();
            if(contentType == null) {
                filterResponseWrapper.outputBuffer();
                return;
            }
            if(!contentType.contains(CONTENT_TYPE_JSON)) {
                filterResponseWrapper.outputBuffer();
                return;
            }
            String result = filterResponseWrapper.getResponseData();
            JSONObject resultJson = JSON.parseObject(result);
            SlogContext slogContext = SlogContext.get();
            List<SqlLog> sqlLogs = slogContext.getSqlLogs();
            resultJson.put("slog", sqlLogs);
            String resultJsonStr = resultJson.toJSONString();
            byte[] bytes = resultJsonStr.getBytes();
            //需要在flush之前修改长度，以及字节.一旦flush长度无法修改，会造成数据被截断
            filterResponseWrapper.setContentLength(bytes.length);
            //重设修改后字节，第一个是重设包装类防止后续拿到有问题，第二个是真实响应浏览器的输出流，不设置无法输出
            filterResponseWrapper.getOutputStream().write(bytes);
            filterResponseWrapper.getResponse().getOutputStream().write(bytes);
        }catch (Exception e) {
            //这里异常只需要输出
            LOGGER.error(e.getMessage(), e);
        }finally {
            //刷新输出流
            filterResponseWrapper.outputBuffer();
            SlogContext.remove();
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //线程变量不存在，说明线程变量未初始化
        if(!SlogContext.exist()) {
            return invocation.proceed();
        }
        long startTime = System.currentTimeMillis();
        String sql = this.getSql(invocation);
        String param = this.getParameterValueString(invocation);
        try {
            //运行sql
            return invocation.proceed();
        } finally {
            SlogContext.add(System.currentTimeMillis() - startTime, sql, param);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private String getSql(Invocation invocation) {
        try {
            Object target = invocation.getTarget();
            StatementHandler statementHandler = (StatementHandler) target;
            BoundSql boundSql = statementHandler.getBoundSql();
            return boundSql.getSql();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String getParameterValueString(Invocation invocation) {
        try {
            Object[] args = invocation.getArgs();
            Object a = args[0];
            Object handler = Proxy.getInvocationHandler(a);
            PreparedStatementLogger pstLogger = (PreparedStatementLogger) handler;
            Class clz = pstLogger.getClass().getSuperclass();
            Method m = null;
            m = clz.getDeclaredMethod("getParameterValueString");
            m.setAccessible(true);
            return (String) m.invoke(pstLogger);
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private boolean checkProperty() {
        if(this.slogProperties == null) {
            return false;
        }
        Boolean active = this.slogProperties.getActive();
        if(active == null || !active) {
            return false;
        }
        Boolean passwordActive = this.slogProperties.getPasswordActive();
        if(passwordActive == null) {
            return false;
        }
        return true;
    }
}
