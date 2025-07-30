package Networking.ObjectProtocol;

import java.io.Serializable;

public class ErrorResponse implements IResponse, Serializable  {
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(Exception exception) {
        this.errorMessage = exception.getMessage();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
