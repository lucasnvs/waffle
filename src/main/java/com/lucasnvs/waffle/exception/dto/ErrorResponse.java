package com.lucasnvs.waffle.exception.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String errorCode,
        int status,
        LocalDateTime timestamp
) {
}

