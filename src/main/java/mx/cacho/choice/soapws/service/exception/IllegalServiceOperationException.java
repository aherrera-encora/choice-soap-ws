package mx.cacho.choice.soapws.service.exception;

public class IllegalServiceOperationException extends ServiceException {

    public IllegalServiceOperationException(String message) {
        super(message);
    }

    public IllegalServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
