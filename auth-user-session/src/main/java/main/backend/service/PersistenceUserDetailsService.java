package main.backend.service;

import main.backend.model.entity.UserEntity;
import main.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// TODO. 实现UserDetailsService用于存储自定义UserDetails: UserEntity
// - PersistenceUserDetailsService 从持久化的DB中获取UserDetails
// - CachingUserDetailsService 从Cache缓存中获取UserDetails
// - InMemoryUserDetailsManager 从Memory内存中获取UserDetails
@Service
public class PersistenceUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public PersistenceUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("No user found with username '%s'.", username)));
    }

    public void save(UserEntity user) {
        this.userRepository.save(user);
    }
}
