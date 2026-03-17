package org.example.finance_tracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record BanksDto(
        @Null
        Long id,
        @NotNull
        String name,
        @NotNull
        int balance
) {

}
