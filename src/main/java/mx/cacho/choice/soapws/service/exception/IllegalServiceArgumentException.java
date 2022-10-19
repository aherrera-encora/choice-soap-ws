package mx.cacho.choice.soapws.service.exception;

public class IllegalServiceArgumentException extends ServiceException {

    public IllegalServiceArgumentException(String message) {
        super(message);
    }

    public IllegalServiceArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
