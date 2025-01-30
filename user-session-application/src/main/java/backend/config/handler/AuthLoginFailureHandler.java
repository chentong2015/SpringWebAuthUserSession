package backend.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

// TODO. 定义用户认证失败后的处理器
@Component
public class AuthLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    // 统计远程IP的请求数量, 避免(限定时间内)多次请求造成的密码破解
    // 0:0:0:0:0:0:0:1 = "127.0.0.1"
    private final ConcurrentHashMap<String, Integer> countIpAddress;

    public AuthLoginFailureHandler() {
        countIpAddress = new ConcurrentHashMap<>();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
            String ipAddress = request.getRemoteAddr();
            int count = countIpAddress.getOrDefault(ipAddress, 0);
            if (count > 5) {
                // 同一个地址尝试了过多次数，锁住账号或者提供认证(验证码)
                System.out.println("You try too many times, Block Account !");
            } else {
                System.out.println("Authentication Failed: " + exception.getMessage());
                countIpAddress.put(ipAddress, count + 1);
            }
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}