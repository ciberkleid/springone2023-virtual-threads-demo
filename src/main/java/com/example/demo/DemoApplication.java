package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    static {
        System.setProperty("httpbin.url", "http://localhost:9091");

        // Max carrier threads pool size: default is 256
        // Default parallelism is max(available processors, maxPoolSize)
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "10");
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
