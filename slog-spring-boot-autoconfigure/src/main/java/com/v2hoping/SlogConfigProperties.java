package com.v2hoping;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by houping wang on 2020/8/21
 *
 * @author houping wang
 */
@ConfigurationProperties(prefix = "slog")
public class SlogConfigProperties {

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
