package com.example.TestSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
@RestController
public class TestSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestSpringApplication.class, args);

        Queue<String> queue = new ConcurrentLinkedQueue<>();


    }


    @GetMapping
    public List<String> getHelloText(){
        return List.of("Hello", "world!");
    }

    @GetMapping("/api")
    public String helloText(){
        return "Hello, API";
    }

}
