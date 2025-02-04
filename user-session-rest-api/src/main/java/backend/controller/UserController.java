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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 注册时根据用户提供的Email发送验证码(包含有效期)
    @PostMapping("/registration/code")
    public ResponseEntity<?> getRegistrationCode(@RequestParam(name = "email") String email) {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(1000, 10000);
        System.out.println("Send code { " + code + " } to email: " + email);

        return ResponseEntity.ok().body("Code has been send to your email");
    }

    // 注册用户持久化到UserDetailsService的存储数据库
    // 请求的UserRequest中应该包含注册的验证码，否则无法完成注册
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

    // 从SecurityContext中获取经过验证的UserDetails
    @GetMapping("/whoami")
    public UserEntity user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) authentication.getPrincipal();
    }

    @GetMapping("/showme/{username}")
    public UserEntity showMe(@PathVariable("username") String username) {
        return this.userService.findUserByUsername(username);
    }
}
