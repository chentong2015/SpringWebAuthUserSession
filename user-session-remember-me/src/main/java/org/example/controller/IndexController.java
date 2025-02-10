package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Home Page";
    }

    @GetMapping("/index")
    public String index() {
        return "Welcome to Index Page";
    }
}