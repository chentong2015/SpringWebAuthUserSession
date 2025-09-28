package backend.model;

public class LoginState {

    private boolean isForAdmin;

    public LoginState(boolean isForAdmin) {
        this.isForAdmin = isForAdmin;
    }

    public boolean isForAdmin() {
        return isForAdmin;
    }

    public void setForAdmin(boolean forAdmin) {
        isForAdmin = forAdmin;
    }
}
