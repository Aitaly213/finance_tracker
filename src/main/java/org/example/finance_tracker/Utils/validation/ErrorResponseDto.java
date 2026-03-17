package org.example.finance_tracker.Utils.validation;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String detailMessage,
        LocalDateTime errorTime
) {
}
