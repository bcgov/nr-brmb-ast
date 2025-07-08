package ca.bc.gov.farms.exception;

public class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super();
    }

    public ServiceException(String reason) {
        super(reason);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
