package backend.exception;

public class ResourceConflictException extends RuntimeException {

    private static final long serialVersionUID = 1791564636123821405L;
    private Long resourceId;

    public ResourceConflictException(Long resourceId, String message) {
        super(message);
        this.resourceId = resourceId;
    }

    public Long getResourceId() {
        return resourceId;
    }
}
