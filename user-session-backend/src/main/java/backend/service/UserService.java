package backend.service;

import backend.model.bean.PasswordChanger;
import backend.model.bean.RoleName;
import backend.model.bean.UserRequest;
import backend.model.entity.RoleEntity;
import backend.model.entity.UserEntity;
import backend.repository.RoleRepository;
import backend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // 注册用户时，持久化存储用户的信息
    // 持久化UserEntity对象之前必须先设置它的ID
    public UserEntity persistUser(UserRequest userRequest) {
        UserEntity user = new UserEntity();
        user.setId(userRequest.getId());
        user.setUsername(userRequest.getUsername());
        user.setPassword(pwdEncoder.encode(userRequest.getPassword()));
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());

        // 为不同类型用户分配不同权限Authority
        List<RoleEntity> authorities = new ArrayList<>();
        authorities.add(roleRepository.findByName(RoleName.ROLE_USER));
        if (userRequest.getUsername().equals("admin")) {
            authorities.add(roleRepository.findByName(RoleName.ROLE_ADMIN));
        }
        user.setAuthorities(authorities);

        return this.userRepository.save(user);
    }

    // 在Auth授权登录成功后，修改用户密码
    public void changePassword(PasswordChanger passwordChanger) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = this.userRepository.findByUsername(currentUser.getName()).orElse(null);
        if (userEntity == null) {
            throw new UsernameNotFoundException("No user found");
        }

        // 由于用户已经登录，因此不用再返回验证老密码
        // String oldPasswordEncoded = pwdEncoder.encode(passwordChanger.getOldPassword());
        // if (!oldPasswordEncoded.equals(userEntity.getPassword())) {
        //     throw new RuntimeException("Old password is wrong");
        // }

        String newPasswordEncoded = pwdEncoder.encode(passwordChanger.getNewPassword());
        userEntity.setPassword(newPasswordEncoded);
        this.userRepository.save(userEntity);
    }

    public UserEntity findUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username).orElse(null);
    }
}
