package backend.service;

import backend.entity.UserEntity;
import backend.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO. 关于ADMIN Role管理员用户的相关操作
@Service
public class UserAdminService {

    private final UserRepository userRepository;

    public UserAdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserEntity findUserById(Long id) throws AccessDeniedException {
        return this.userRepository.findById(id).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserEntity> findAllUsers() throws AccessDeniedException {
        return this.userRepository.findAll();
    }
}
