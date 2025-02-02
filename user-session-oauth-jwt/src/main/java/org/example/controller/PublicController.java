package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/")
public class PublicController {

    @GetMapping("/search")
    public String publicSpace() {
          return "Open Searching Space";
    }
}
