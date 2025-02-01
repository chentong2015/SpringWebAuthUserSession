package backend.model.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class InvalidAuthentication extends AbstractAuthenticationToken {

    public InvalidAuthentication() {
        super(null);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    // TODO. 请求无法被正确的认证成功
    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public int hashCode() {
        return 7;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return getClass() == obj.getClass();
    }
}
