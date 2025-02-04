package org.example.controller;

import org.example.model.CustomUser;
import org.example.model.Response;
import org.example.model.StatusCode;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    // TODO: 使用Spring Security提供的InMemoryUserService
    private final InMemoryUserDetailsManager inMemoryUserDetails;
    private final PasswordEncoder passwordEncoder;

    public UserController(InMemoryUserDetailsManager inMemoryUserDetails, PasswordEncoder passwordEncoder) {
        this.inMemoryUserDetails = inMemoryUserDetails;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Response registerUser(@RequestBody CustomUser customUser) {
        UserDetails newUser = User.builder()
                .username(customUser.getUsername())
                .password(this.passwordEncoder.encode(customUser.getPassword()))
                .roles(customUser.getRole())
                .build();
        this.inMemoryUserDetails.createUser(newUser);
        return new Response(StatusCode.SUCCESS, "Add Success", newUser);
    }

    @GetMapping("/{username}")
    public Response findUserByName(@PathVariable String username) {
        UserDetails userDetails = this.inMemoryUserDetails.loadUserByUsername(username);
        return new Response(StatusCode.SUCCESS, "Find One Success", userDetails);
    }

    @DeleteMapping("/{username}")
    public Response deleteUser(@PathVariable String username) {
        this.inMemoryUserDetails.deleteUser(username);
        return new Response(StatusCode.SUCCESS, "Delete Success", null);
    }

    @PostMapping("/{username}/password")
    public Response changePassword(@PathVariable String username, @RequestBody Map<String, String> passwordMap) {
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");
        String confirmNewPassword = passwordMap.get("confirmNewPassword");

        this.inMemoryUserDetails.changePassword(oldPassword, newPassword);
        return new Response(StatusCode.SUCCESS, "Change Password Success", null);
    }
}
