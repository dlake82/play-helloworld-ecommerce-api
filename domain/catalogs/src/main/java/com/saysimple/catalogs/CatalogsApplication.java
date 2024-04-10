package com.saysimple.catalogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CatalogsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogsApplication.class, args);
    }

}
