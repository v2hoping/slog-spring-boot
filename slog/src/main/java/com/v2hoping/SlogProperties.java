package com.v2hoping;

/**
 * Created by houping wang on 2020/8/20
 *
 * @author houping wang
 */
public class SlogProperties {

    /**
     * 是否启用，默认不启用
     */
    private Boolean active;

    /**
     * 是否密码模式
     */
    private Boolean passwordActive;

    /**
     * 密码，为空则不使用密码
     */
    private String password;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getPasswordActive() {
        return passwordActive;
    }

    public void setPasswordActive(Boolean passwordActive) {
        this.passwordActive = passwordActive;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


