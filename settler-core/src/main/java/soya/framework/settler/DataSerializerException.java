package soya.framework.settler;

public class DataSerializerException extends RuntimeException {
    public DataSerializerException() {
    }

    public DataSerializerException(String message) {
        super(message);
    }

    public DataSerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSerializerException(Throwable cause) {
        super(cause);
    }
}
