package backend.session.token;

public class TokenState {

    private boolean isAdmin;
    private String access_token;
    private Long expires_in;

    public TokenState(boolean isAdmin, String access_token, long expires_in) {
        this.isAdmin = isAdmin;
        this.access_token = access_token;
        this.expires_in = expires_in;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }
}
