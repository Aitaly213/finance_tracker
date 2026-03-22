package org.example.finance_tracker.utils.validation;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String detailMessage,
        LocalDateTime errorTime
) {
}
