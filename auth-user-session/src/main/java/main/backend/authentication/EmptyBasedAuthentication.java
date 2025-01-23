package main.backend.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class EmptyBasedAuthentication extends AbstractAuthenticationToken {

    public EmptyBasedAuthentication() {
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

    // 基于Empty的认证成功
    @Override
    public boolean isAuthenticated() {
        return true;
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
