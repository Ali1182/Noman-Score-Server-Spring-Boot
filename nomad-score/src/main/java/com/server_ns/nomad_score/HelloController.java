package com.server_ns.nomad_score;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    // Testing Hello world sequences
    @GetMapping("/hello/testing") 
    public String helloTesting() {
        return "Hello Testing";
    }
    
    @GetMapping("/") 
    public String index() {
        return "This was being very ennoying, Oh well";
    }
} 
