package com.saysimple.configs.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ConfigController {

    @GetMapping
    public String status() {
        return "Hello from Config Server";
    }
}
