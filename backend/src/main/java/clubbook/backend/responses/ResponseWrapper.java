package clubbook.backend.responses;

/**
 * A generic wrapper class for API responses.
 * This class encapsulates a response message and the associated data.
 *
 * @param <T> the type of the data contained in the response
 */
public class ResponseWrapper<T> {

    /**
     * Message describing the response.
     */
    private String message;

    /**
     * Data to be included in the response.
     */
    private T data;

    /**
     * Constructs a new ResponseWrapper with the specified message and data.
     *
     * @param message a message describing the response
     * @param data the data to be included in the response
     */
    public ResponseWrapper(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
