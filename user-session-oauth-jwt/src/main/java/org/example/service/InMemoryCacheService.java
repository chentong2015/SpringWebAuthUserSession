package org.example.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class InMemoryCacheService {

    private ConcurrentHashMap<String, String> cacheUserToken;

    public InMemoryCacheService() {
        this.cacheUserToken = new ConcurrentHashMap<>();
    }

    public void setToken(String username, String token) {
        this.cacheUserToken.put(username, token);
    }

    // TODO. 推荐设置Token在缓存中的过期时间
    public void setToken(String username, String token, long timeout, TimeUnit timeUnit) {
        this.cacheUserToken.put(username, token);
    }

    // 验证用的Token数据存在
    public boolean existToken(String username, String token) {
        String existToken = this.cacheUserToken.getOrDefault(username, null);
        return existToken != null && existToken.equals(token);
    }
}
