package com.cyecize.reporter.common.exceptions;

import com.cyecize.http.HttpStatus;

public class JsonException extends Exception {

    private HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, HttpStatus status) {
        super(message);
        this.statusCode = status;
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.statusCode = status;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
