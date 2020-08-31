package com.v2hoping;

import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * Created by houping wang on 2020/8/20
 *
 * @author houping wang
 */
@Configuration
@ConditionalOnProperty(prefix = "slog", name = "active", havingValue = "true")
@EnableConfigurationProperties(SlogConfigProperties.class)
@ConditionalOnClass(value = {Interceptor.class, Filter.class})
public class SlogConfiguration {

    @Bean
    @ConditionalOnMissingBean(SlogInterceptor.class)
    public SlogInterceptor slogInterceptor(SlogConfigProperties slogConfigProperties) {
        SlogProperties slogProperties = new SlogProperties();
        slogProperties.setActive(slogConfigProperties.getActive());
        slogProperties.setPasswordActive(slogConfigProperties.getPasswordActive());
        slogProperties.setPassword(slogConfigProperties.getPassword());
        if(slogConfigProperties.getPassword() == null) {
            slogProperties.setPasswordActive(false);
        }
        return new SlogInterceptor(slogProperties);
    }

}
