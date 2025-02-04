package org.example.auth;

import org.example.util.UrlPathParser;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import java.util.function.Supplier;

// TODO. 在Auth成功后为URL添加特定权限控制
// - 用户必须访问它自己的Url链接
// - 用户必须满足特定Role角色, 否则403 Forbidden
@Component
public class UserRequestAuthManager implements AuthorizationManager<RequestAuthorizationContext> {

    // TODO. OAuth2以JWT Token驱动用户验证, Principal对应Jwt object
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        Authentication authentication = authenticationSupplier.get();

        // Extract the userId from the Authentication object, which is a Jwt object
        String usernameJwt = ((Jwt) authentication.getPrincipal()).getClaim("username").toString();
        String usernameUrl = UrlPathParser.parseUsername(context);

        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_admin"));
        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_user"));

        boolean userIdsMatch = usernameUrl != null && usernameUrl.equals(usernameJwt);
        return new AuthorizationDecision(hasAdminRole || hasUserRole && userIdsMatch);
    }
}
