package main.Exception;

import java.io.Serializable;

/**
 * Custom exception class for handling service layer errors.
 * This exception is thrown when business logic or service operations fail.
 */
public class ServiceException extends RuntimeException implements Serializable {

  /**
   * Default constructor for ServiceException.
   * Initializes the exception with no message or cause.
   */
  public ServiceException() {
      super();
  }

  /**
   * Constructor for ServiceException that accepts a custom message.
   *
   * @param message the detail message to be associated with the exception
   */
  public ServiceException(String message) {
      super(message);
  }

  /**
   * Constructor for ServiceException that accepts a custom message and a cause.
   *
   * @param message the detail message to be associated with the exception
   * @param cause the cause of the exception (usually another throwable)
   */
  public ServiceException(String message, Throwable cause) {
      super(message, cause);
  }

  /**
   * Constructor for ServiceException that accepts a cause.
   *
   * @param cause the cause of the exception (usually another throwable)
   */
  public ServiceException(Throwable cause) {
      super(cause);
  }

  /**
   * Constructor for ServiceException that accepts a message, cause, and options to enable suppression and
   * make the stack trace writable.
   *
   * @param message the detail message to be associated with the exception
   * @param cause the cause of the exception (usually another throwable)
   * @param enableSuppression whether or not suppression is enabled
   * @param writableStackTrace whether or not the stack trace is writable
   */
  protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
  }
}
