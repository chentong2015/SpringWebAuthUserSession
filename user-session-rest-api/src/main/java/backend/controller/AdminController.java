package backend.controller;

import backend.model.entity.UserEntity;
import backend.service.UserAdminService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private final UserAdminService adminService;

    public AdminController(UserAdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/user/{userId}")
    public UserEntity loadUserById(@PathVariable("userId") Long userId) {
        return this.adminService.findUserById(userId);
    }

    @GetMapping("/user/all")
    public List<UserEntity> loadAllUsers() {
        return this.adminService.findAllUsers();
    }
}
