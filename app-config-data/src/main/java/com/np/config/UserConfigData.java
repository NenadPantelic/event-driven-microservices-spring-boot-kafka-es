package com.np.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("user-config")
public class UserConfigData {

    private String username;
    private String password;
    private String[] roles;
}
