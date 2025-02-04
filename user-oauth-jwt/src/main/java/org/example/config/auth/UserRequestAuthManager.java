package org.example.config.auth;

import org.example.util.UrlPathParser;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

// TODO. 在Auth成功后为URL添加特定权限控制
// - 用户必须访问它自己的Url链接
// - 用户必须满足特定Role角色, 否则403 Forbidden
@Component
public class UserRequestAuthManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        // 从Authentication中获取Jwt object, 并验证用于名称和Role
        Authentication authentication = authenticationSupplier.get();
        Jwt jwtObject = (Jwt) authentication.getPrincipal();

        String usernameJwt = jwtObject.getClaim("username").toString();
        String usernameUrl = UrlPathParser.parseUsername(context);
        boolean userIdsMatch = usernameUrl != null && usernameUrl.equals(usernameJwt);

        // 从Authentication中直接获取GrantedAuthority
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();

        // 解析用户的多个Role角色
        String[] authorities = jwtObject.getClaim("authorities").toString().split(" ");
        boolean hasAdminRole = Arrays.asList(authorities).contains("ROLE_ADMIN");
        boolean hasUserRole = Arrays.asList(authorities).contains("ROLE_USER");

        return new AuthorizationDecision(hasAdminRole || hasUserRole && userIdsMatch);
    }
}
