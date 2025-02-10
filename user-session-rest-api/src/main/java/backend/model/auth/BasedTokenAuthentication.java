package backend.model.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class BasedTokenAuthentication extends AbstractAuthenticationToken {

    private final String token;
    private final UserDetails principle;

    public BasedTokenAuthentication(UserDetails principle, String token) {
        super(principle.getAuthorities());
        this.principle = principle;
        this.token = token;
    }

    // TODO. 说明基于Token的用户认证成功
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public UserDetails getPrincipal() {
        return principle;
    }

}
