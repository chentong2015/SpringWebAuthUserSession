package backend.exception;

public class ResourceConflictException extends RuntimeException {

    private static final long serialVersionUID = 1791564636123821405L;
    private final String resourceUsername;

    public ResourceConflictException(String resourceUsername, String message) {
        super(message);
        this.resourceUsername = resourceUsername;
    }

    public String getResourceUsername() {
        return resourceUsername;
    }
}
