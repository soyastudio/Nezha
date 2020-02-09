package soya.framework.nezha;

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
