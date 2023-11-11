package com.np.configserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        // passwords encrypted with spring boot cli can be fetched with these endpoints
        webSecurity.ignoring()
                .antMatchers("/encrypt/**", "/decrypt/**");

        super.configure(webSecurity);
    }
}
