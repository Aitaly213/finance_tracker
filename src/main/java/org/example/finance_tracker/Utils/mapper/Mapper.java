package org.example.finance_tracker.Utils.mapper;

import org.example.finance_tracker.dto.BanksDto;
import org.example.finance_tracker.dto.CategoryDto;
import org.example.finance_tracker.dto.CreateTransactionDto;
import org.example.finance_tracker.dto.TransactionResponseDto;
import org.example.finance_tracker.entity.BanksEntity;
import org.example.finance_tracker.entity.CategoryEntity;
import org.example.finance_tracker.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public CreateTransactionDto toDtoCreateTransaction(TransactionEntity entity) {
        return new CreateTransactionDto(
                entity.getId(),
                entity.getCategory().getId(),
                entity.getBank().getId(),
                entity.getAmountOfMoney(),
                entity.getDate(),
                entity.getTime(),
                entity.getTransactionType()
        );
    }

    public TransactionResponseDto toDtoTransactionResponse(TransactionEntity entity) {
        return new TransactionResponseDto(
                entity.getId(),
                toDtoCategory(entity.getCategory()),
                toDtoBank(entity.getBank()),
                entity.getAmountOfMoney(),
                entity.getDate(),
                entity.getTime(),
                entity.getTransactionType()
        );
    }

    public CategoryDto toDtoCategory(CategoryEntity entity) {
        return new CategoryDto(
                entity.getId(), entity.getTitle(), entity.getEmoji()
        );
    }

    public BanksDto toDtoBank(BanksEntity entity) {
        return new BanksDto(
                entity.getId(), entity.getName(), entity.getBalance()
        );
    }

}
