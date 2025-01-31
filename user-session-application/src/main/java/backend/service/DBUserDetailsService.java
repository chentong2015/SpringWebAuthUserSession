package backend.service;

import backend.model.entity.UserEntity;
import backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// TODO. UserDetailsService 提供UserDetails的来源
// 用于获取UserDetails并提供给AuthenticationManager进行验证
// - PersistenceUserDetailsService 从持久化DB中获取UserDetails
// - CachingUserDetailsService     从Cache缓存获取UserDetails
// - InMemoryUserDetailsManager    从Memory内存获取UserDetails
@Service
public class DBUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DBUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Hibernate 底层会执行的Query
    // select id,firstname,lastname,password,username from t_user where username=?
    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isEmpty()) {
            String msg = String.format("No user found with username '%s'.", username);
            throw new UsernameNotFoundException(msg);
        }
        return userEntity.get();
    }

    public void save(UserEntity user) {
        this.userRepository.save(user);
    }
}
