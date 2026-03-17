package org.example.finance_tracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.List;

public record CategoryDto(
        @Null
        Long id,
        @NotNull
        String title,
        @NotNull
        String emoji
) {
}
