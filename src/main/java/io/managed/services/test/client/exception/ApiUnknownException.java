package io.managed.services.test.client.exception;

import java.util.List;
import java.util.Map;

public class ApiUnknownException extends Exception {

    public ApiUnknownException(
        String message,
        Exception cause) {

        super(message, cause);
    }

    public String getFullMessage() {
        var error = new StringBuilder();
        error.append(getMessage());
        return error.toString();
    }
}
