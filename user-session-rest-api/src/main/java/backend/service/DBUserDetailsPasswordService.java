package backend.service;

import backend.model.entity.UserEntity;
import backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// TODO. 定义AuthenticationManager修改UserDetails密码的实现方式
@Service
public class DBUserDetailsPasswordService implements UserDetailsPasswordService {

    private final UserRepository userRepository;

    public DBUserDetailsPasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Optional<UserEntity> userExist = this.userRepository.findByUsername(user.getUsername());
        if (userExist.isEmpty()) {
            throw new UsernameNotFoundException("No User found");
        }

        UserEntity userEntity = userExist.get();
        userEntity.setPassword(newPassword);
        this.userRepository.save(userEntity);
        return userEntity;
    }
}
