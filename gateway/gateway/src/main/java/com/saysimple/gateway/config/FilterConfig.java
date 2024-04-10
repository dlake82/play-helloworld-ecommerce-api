package com.saysimple.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FilterConfig {
    Environment env;
    public FilterConfig(Environment env){
        this.env = env;
    }
}
