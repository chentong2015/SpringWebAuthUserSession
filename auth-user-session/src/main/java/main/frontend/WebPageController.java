package main.frontend;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Web Page 前端UI访问的页面
@Controller
public class WebPageController {

    @GetMapping("/")
    @PreAuthorize("permitAll()")
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
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return "user.html";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage() {
        return "/admin.html";
    }

    @GetMapping("/public-secure")
    @PreAuthorize("hasRole('USER')")
    public String publicSecurePage() {
        return "public-secure.html";
    }
}
