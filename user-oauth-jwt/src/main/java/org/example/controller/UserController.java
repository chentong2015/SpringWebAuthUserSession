package org.example.controller;

import org.example.model.CustomUser;
import org.example.model.Response;
import org.example.model.StatusCode;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    // TODO: 使用Spring Security提供的InMemoryUserService
    private final InMemoryUserDetailsManager inMemoryUserDetails;

    public UserController(InMemoryUserDetailsManager inMemoryUserDetails) {
        this.inMemoryUserDetails = inMemoryUserDetails;
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
