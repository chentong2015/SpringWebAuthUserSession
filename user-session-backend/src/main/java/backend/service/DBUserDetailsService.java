package backend.service;

import backend.model.entity.UserEntity;
import backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// TODO. UserDetails存储以来源的实现
// 用于获取UserDetails并提供给AuthenticationManager进行验证
//
// TODO. UserDetailsService的三种实现
// - PersistenceUserDetailsService 从持久化的DB中获取UserDetails
// - CachingUserDetailsService 从Cache缓存中获取UserDetails
// - InMemoryUserDetailsManager 从Memory内存中获取UserDetails
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
        String msg = String.format("No user found with username '%s'.", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(msg));
    }

    public void save(UserEntity user) {
        this.userRepository.save(user);
    }
}
