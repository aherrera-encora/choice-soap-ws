package mx.cacho.choice.soapws.endpoint.exception;

public class HotelNotFoundException extends ReceiverException {

    public HotelNotFoundException(String message) {
        super(message);
    }

    public HotelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
