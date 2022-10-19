package mx.cacho.choice.soapws.endpoint.exception;

public class AmenityNotFoundException extends ReceiverException {

    public AmenityNotFoundException(String message) {
        super(message);
    }

    public AmenityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
