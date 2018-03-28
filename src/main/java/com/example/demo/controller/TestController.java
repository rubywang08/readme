package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/helloworld")
    public String helloWorld() {
        return "hello world";
    }
    
    @GetMapping("/helloworld2")
    public String helloWorld2() {
        return "hello world2";
    }
}
