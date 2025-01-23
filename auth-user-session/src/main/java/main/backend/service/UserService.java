package main.backend.service;

import main.backend.model.PasswordChanger;
import main.backend.model.RoleName;
import main.backend.model.UserRequest;
import main.backend.model.entity.RoleEntity;
import main.backend.model.entity.UserEntity;
import main.backend.repository.RoleRepository;
import main.backend.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder globalPasswordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.globalPasswordEncoder = passwordEncoder;
    }

    public UserEntity persistUser(UserRequest userRequest) {
        UserEntity user = new UserEntity();
        user.setUsername(userRequest.getUsername());
        user.setPassword(globalPasswordEncoder.encode(userRequest.getPassword()));
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());

        // TODO. 为不同类型的用户分配不同的权限Authority
        List<RoleEntity> authorities = new ArrayList<>();
        authorities.add(roleRepository.findByName(RoleName.ROLE_USER));
        if (userRequest.getUsername().equals("admin")) {
            authorities.add(roleRepository.findByName(RoleName.ROLE_ADMIN));
        }
        user.setAuthorities(authorities);

        return this.userRepository.save(user);
    }

    // TODO. 必须验证提供的Old密码是当前用户密码才能修改 !!
    public void changePassword(PasswordChanger passwordChanger) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String username = currentUser.getName();
        String userPassword = (String) currentUser.getCredentials();
        String oldPassword = passwordChanger.getOldPassword();

        UserEntity userEntity = this.userRepository.findByUsername(username).orElse(null);
        if (userEntity == null) {
            throw new UsernameNotFoundException("No user found");
        }

        String newPasswordEncoded = globalPasswordEncoder.encode(passwordChanger.getNewPassword());
        userEntity.setPassword(newPasswordEncoded);
        this.userRepository.save(userEntity);
    }

    // 必须要User Role角色才能查看用户数据
    @PreAuthorize("hasRole('USER')")
    public UserEntity findUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    // 必须要ADMIN Role角色才能查看用户数据
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntity findUserById(Long id) throws AccessDeniedException {
        return this.userRepository.findById(id).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserEntity> findAllUsers() throws AccessDeniedException {
        return this.userRepository.findAll();
    }
}
