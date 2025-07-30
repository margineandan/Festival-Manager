package Domain.Validators;

import java.io.Serializable;

/**
 * Custom exception class for handling validation errors.
 * This exception is thrown when validation rules for entities are not met.
 */
public class ValidationException extends RuntimeException implements Serializable {

    /**
     * Default constructor for ValidationException.
     * Initializes the exception with no message or cause.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructor for ValidationException that accepts a custom message.
     *
     * @param message the detail message to be associated with the exception
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor for ValidationException that accepts a custom message and a cause.
     *
     * @param message the detail message to be associated with the exception
     * @param cause the cause of the exception (usually another throwable)
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for ValidationException that accepts a cause.
     *
     * @param cause the cause of the exception (usually another throwable)
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor for ValidationException that accepts a message, cause, and options to enable suppression and
     * make the stack trace writable.
     *
     * @param message the detail message to be associated with the exception
     * @param cause the cause of the exception (usually another throwable)
     * @param enableSuppression whether or not suppression is enabled
     * @param writableStackTrace whether or not the stack trace is writable
     */
    protected ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

