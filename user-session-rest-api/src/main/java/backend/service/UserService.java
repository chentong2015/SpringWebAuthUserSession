package backend.service;

import backend.model.PasswordChangeRequest;
import backend.model.RoleName;
import backend.model.UserRequest;
import backend.model.entity.RoleEntity;
import backend.model.entity.UserEntity;
import backend.repository.RoleRepository;
import backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// TODO. 关于User用户的相关操作
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder pwdEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.pwdEncoder = passwordEncoder;
    }

    public UserEntity findUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    // 注册用户时，持久化存储用户的信息
    public UserEntity persistUser(UserRequest userRequest) {
        UserEntity user = new UserEntity();
        user.setUsername(userRequest.getUsername());
        user.setPassword(pwdEncoder.encode(userRequest.getPassword()));
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());

        // TODO. 为不同类型用户分配不同权限Authority
        List<RoleEntity> authorities = new ArrayList<>();
        authorities.add(roleRepository.findByName(RoleName.ROLE_USER));
        if (userRequest.getUsername().equals("admin")) {
            authorities.add(roleRepository.findByName(RoleName.ROLE_ADMIN));
        }
        user.setAuthorities(authorities);

        return this.userRepository.save(user);
    }

    // 在Auth授权登录成功后，修改用户密码
    public void changePassword(PasswordChangeRequest passwordChanger) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = this.userRepository.findByUsername(currentUser.getName()).orElse(null);
        if (userEntity == null) {
            throw new UsernameNotFoundException("No user found");
        }

        // 验证提供的老密码是否正确
        String oldPasswordEncoded = pwdEncoder.encode(passwordChanger.getOldPassword());
        if (!oldPasswordEncoded.equals(userEntity.getPassword())) {
            throw new RuntimeException("Old password is wrong");
        }

        // 必须验证密码符合特定的Policy
        if (passwordChanger.getNewPassword().isEmpty()) {
            throw new RuntimeException("New password cannot be empty");
        }

        String newPasswordEncoded = pwdEncoder.encode(passwordChanger.getNewPassword());
        userEntity.setPassword(newPasswordEncoded);
        this.userRepository.save(userEntity);
    }
}
