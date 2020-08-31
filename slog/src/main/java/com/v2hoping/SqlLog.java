package com.v2hoping;

import java.util.List;

/**
 * Created by houping wang on 2020/8/20
 *
 * @author houping wang
 */
public class SqlLog {

    /**
     * 耗时
     */
    private Long time;

    /**
     * 执行SQL
     */
    private String sql;

    /**
     * 参数
     */
    private String param;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
