package org.example.controller;

import org.example.model.Response;
import org.example.model.StatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    // TODO: 使用Spring Security提供的InMemoryUserService
    private final InMemoryUserDetailsManager inMemoryUserDetails;

    public UserController(InMemoryUserDetailsManager inMemoryUserDetails) {
        this.inMemoryUserDetails = inMemoryUserDetails;
    }

    // TODO. 先执行preHandler拦截器的逻辑，然后指定Endpoint逻辑
    @GetMapping("/{username}")
    public Response findUserByName(@PathVariable("username") String username) {
        UserDetails userDetails = this.inMemoryUserDetails.loadUserByUsername(username);
        return new Response(StatusCode.SUCCESS, "Find One Success", userDetails);
    }

    @DeleteMapping("/{username}")
    public Response deleteUser(@PathVariable("username") String username) {
        this.inMemoryUserDetails.deleteUser(username);
        return new Response(StatusCode.SUCCESS, "Delete Success", null);
    }

    @PostMapping("/refresh/password")
    public Response changePassword(@RequestBody Map<String, String> passwordMap) {
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");
        String confirmNewPassword = passwordMap.get("confirmNewPassword");

        this.inMemoryUserDetails.changePassword(oldPassword, newPassword);
        return new Response(StatusCode.SUCCESS, "Change Password Success", null);
    }
}
