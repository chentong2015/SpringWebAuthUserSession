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
    // 因为是在用户空间完成的，因此不在需要原始的密码
    public void changePassword(PasswordChangeRequest passwordChanger) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = this.userRepository.findByUsername(currentUser.getName()).orElse(null);
        if (userEntity == null) {
            throw new UsernameNotFoundException("No user found");
        }

        // 必须验证密码符合特定的Policy
        if (passwordChanger.getNewPassword().isEmpty()) {
            throw new RuntimeException("New password cannot be empty");
        }

        // 必须满足两次的密码完全一致
        if (!passwordChanger.getNewPassword().equals(passwordChanger.getConfirmPassword())) {
            throw new RuntimeException("New and confirm passwords are not the same.");
        }

        String newPasswordEncoded = pwdEncoder.encode(passwordChanger.getNewPassword());
        userEntity.setPassword(newPasswordEncoded);
        this.userRepository.save(userEntity);
    }
}
