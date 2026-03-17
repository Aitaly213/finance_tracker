package org.example.finance_tracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import org.example.finance_tracker.entity.TransactionType;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateTransactionDto(
        @Null
        Long id,
        @NotNull
        Long categoryId,
        @NotNull
        Long bankId,
        @NotNull
        Long amount,
        @NotNull
        @PastOrPresent
        LocalDate date,

        LocalTime time,
        @NotNull
        TransactionType transactionType

) {
}
