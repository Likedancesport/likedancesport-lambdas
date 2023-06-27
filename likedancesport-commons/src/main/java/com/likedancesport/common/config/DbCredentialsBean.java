package com.likedancesport.common.config;

import com.likedancesport.common.annotation.InjectSsmParameter;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
@ToString
@Slf4j
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

    @PostConstruct
    private void test() {
        log.info("post contruct");
    }
}
