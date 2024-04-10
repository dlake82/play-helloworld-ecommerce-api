package com.saysimple.gateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")

public class GatewayController {

    Environment env;

    @Autowired
    public GatewayController(Environment env) {
        this.env = env;

    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return String.format("It's Working in Gateway Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time")
        );
    }
}
