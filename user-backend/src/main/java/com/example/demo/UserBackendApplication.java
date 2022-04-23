package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Random;

@SpringBootApplication
public class UserBackendApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(UserBackendApplication.class, args);

        Random rd = (Random) context.getBean("beanRandom");
        System.out.println(rd.nextInt(1000));
    }
}
