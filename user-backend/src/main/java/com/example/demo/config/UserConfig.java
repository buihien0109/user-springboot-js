package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class UserConfig {

    @Bean("beanRandom")
    public Random random() {
        return new Random();
    }
}
