package com.example.mutualssl;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MyController {
    @GetMapping("/hello")
    public String sayHello(){
        return "Hello, Spring Boot supposed to make mutual ssl easier";
    }
}
