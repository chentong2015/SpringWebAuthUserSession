package backend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    // TODO. 用户登出后直接重定向到Login登录页面
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        Map<String, String> result = new HashMap<>();
        result.put("Result", "Logout Successfully");

        response.setContentType("application/json");
        response.sendRedirect("/login.html");

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
