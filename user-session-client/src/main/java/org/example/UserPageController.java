package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Web Page 前端UI访问的页面
@Controller
public class UserPageController {

    @GetMapping("/")
    public String home() {
        return "redirect:login";
    }

    @GetMapping("/login")
    public String login() {
        return "/login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "/register.html";
    }

    @GetMapping("/public")
    public String publicPage() {
        return "public.html";
    }

    @GetMapping("/user")
    public String user() {
        return "user.html";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "/admin.html";
    }

    @GetMapping("/public-secure")
    public String publicSecurePage() {
        return "public-secure.html";
    }
}
