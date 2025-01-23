package main.backend.controller;

import main.backend.model.entity.UserEntity;
import main.backend.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    public UserEntity loadUserById(@PathVariable Long userId) {
        return this.userService.findUserById(userId);
    }

    @GetMapping("/user/all")
    public List<UserEntity> loadAllUsers() {
        return this.userService.findAllUsers();
    }
}
