package com.saysimple.users.error.exception;

import org.apache.http.client.HttpResponseException;

public class NotFoundException extends HttpResponseException {
    public NotFoundException(String message) {
        super(404, message);
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message must not be null or empty");
        }
    }
}
