package com.henningstorck.springbootvite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringBootViteApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootViteApplication.class, args);
    }
}
