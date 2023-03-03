package com.likedancesport.config;

import com.likedancesport.common.parameter.annotation.InjectSsmParameter;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
@ToString
public class DbCredentialsBean {
    @InjectSsmParameter(parameterName = "like-db-url")
    private String dbUrl;
    @InjectSsmParameter(parameterName = "like-db-user")
    private String dbUsername;
    @InjectSsmParameter(parameterName = "like-db-password", encrypted = true)
    private String dbPassword;

    @Autowired
    public DbCredentialsBean() {
    }
}
