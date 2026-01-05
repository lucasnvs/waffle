package com.lucasnvs.waffle.common.exception.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponse {
    private String message;

    @JsonProperty("errorCode")
    private String errorCode;

    private int status;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

    public ValidationErrorResponse(String message, String errorCode, int status, LocalDateTime timestamp, Map<String, String> errors) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}

