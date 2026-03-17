package org.example.finance_tracker.controller;

import jakarta.validation.Valid;
import org.example.finance_tracker.dto.CreateTransactionDto;
import org.example.finance_tracker.dto.StatisticsDto;
import org.example.finance_tracker.dto.TransactionResponseDto;
import org.example.finance_tracker.service.TransactionsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {

    private final TransactionsService service;

    public TransactionsController(TransactionsService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<CreateTransactionDto> createTransaction(
            @RequestBody @Valid CreateTransactionDto transactionToCreate
    ) {
        return ResponseEntity.ok().body(service.createTransaction(transactionToCreate));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getTransactions(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 10, sort = "date") Pageable pageable
    ) {
        return ResponseEntity.ok(
                service.getTransactions(month, categoryId, pageable)
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<StatisticsDto> getStats(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long bankId
    ){
        return ResponseEntity.ok(
                service.getStatistics(month,categoryId,bankId)
        );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactions(
            @PathVariable Long id
    ) {
        service.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateTransactionDto> updateTransaction(
            @PathVariable Long id,
            @RequestBody CreateTransactionDto updatedTransaction
    ) {
        return ResponseEntity.ok().body(service.updateTransaction(id, updatedTransaction));
    }

}
