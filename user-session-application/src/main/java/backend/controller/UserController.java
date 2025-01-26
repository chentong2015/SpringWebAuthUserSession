package backend.controller;

import backend.exception.ResourceConflictException;
import backend.model.bean.PasswordChanger;
import backend.model.bean.UserRequest;
import backend.model.entity.UserEntity;
import backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 当抛出异常时返回Exception ResponseEntity
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
        UserEntity existUser = this.userService.findUserByUsername(userRequest.getUsername());
        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists");
        }

        UserEntity user = this.userService.persistUser(userRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
        this.userService.changePassword(passwordChanger);

        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        return ResponseEntity.accepted().body(result);
    }

    // 确保具有"ROLE_USER", 从ContextHolder中获取认证过的UserDetails
    @GetMapping("/whoami")
    public UserEntity user() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/showme/{username}")
    public UserEntity showMe(@PathVariable("username") String username) {
        return this.userService.findUserByUsername(username);
    }
}
