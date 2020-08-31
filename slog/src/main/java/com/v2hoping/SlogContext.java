package com.v2hoping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houping wang on 2020/8/20
 *
 * @author houping wang
 */
public class SlogContext {

    private static final ThreadLocal<SlogContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private boolean flag = false;

    private List<SqlLog> sqlLogs = new ArrayList<>();

    public List<SqlLog> getSqlLogs() {
        return sqlLogs;
    }

    public void setSqlLogs(List<SqlLog> sqlLogs) {
        this.sqlLogs = sqlLogs;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void addSqlLogs(Long time, String sql, String param) {
        SqlLog sqlLog = new SqlLog();
        sqlLog.setTime(time);
        sqlLog.setSql(sql);
        sqlLog.setParam(param);
        this.sqlLogs.add(sqlLog);
    }

    public static void init() {
        SlogContext slogContext = new SlogContext();
        CONTEXT_THREAD_LOCAL.set(slogContext);
    }

    public static boolean exist() {
        return CONTEXT_THREAD_LOCAL.get() != null;
    }

    public static void add(Long time, String sql, String param) {
        if(!exist()) {
            return;
        }
        CONTEXT_THREAD_LOCAL.get().addSqlLogs(time, sql, param);
    }

    public static SlogContext get() {
        return CONTEXT_THREAD_LOCAL.get();
    }

    public static void remove() {
        CONTEXT_THREAD_LOCAL.remove();
    }

}
