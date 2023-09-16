package com.example.chatcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChatCheckApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatCheckApplication.class, args);
    }

}
