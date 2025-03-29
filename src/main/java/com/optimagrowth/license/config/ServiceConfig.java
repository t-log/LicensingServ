package com.optimagrowth.license.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "example")
@Getter
@Setter
@Slf4j
public class ServiceConfig{

    private String property;

    public String getProperty(){
        log.info("Propery value is :"+property);
        return property;

    }
}
