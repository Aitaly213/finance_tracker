package org.example.finance_tracker.dto;

import org.example.finance_tracker.entity.BanksEntity;
import org.example.finance_tracker.entity.TransactionType;

import java.time.LocalDate;
import java.time.LocalTime;

public record TransactionResponseDto(
        Long id,
        CategoryDto category,
        BanksDto banksDto,
        Long amount,
        LocalDate date,
        LocalTime time,
        TransactionType transactionType
) {
}
