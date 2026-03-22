package org.example.finance_tracker.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.finance_tracker.utils.mapper.Mapper;
import org.example.finance_tracker.dto.CreateTransactionDto;
import org.example.finance_tracker.dto.StatisticsDto;
import org.example.finance_tracker.dto.TransactionResponseDto;
import org.example.finance_tracker.entity.TransactionEntity;
import org.example.finance_tracker.entity.TransactionType;
import org.example.finance_tracker.entity.UserEntity;
import org.example.finance_tracker.repository.BalanceRepository;
import org.example.finance_tracker.repository.CategoryRepository;
import org.example.finance_tracker.repository.TransactionRepository;
import org.example.finance_tracker.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Objects;

@Service
public class TransactionsService {

    private final TransactionRepository repository;
    private final CategoryRepository categoryRepository;
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    public TransactionsService(TransactionRepository repository, CategoryRepository categoryRepository, BalanceRepository balanceRepository, UserRepository userRepository, Mapper mapper) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.balanceRepository = balanceRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    private UserEntity getCurrentUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        UserEntity user = getCurrentUser();

        return repository.findAllByUserId(user.getId(), pageable)
                .map(mapper::toDtoTransactionResponse);
    }

    @Transactional
    public CreateTransactionDto createTransaction(CreateTransactionDto transactionToCreate) {

        UserEntity user = getCurrentUser();

        var bankEntity = balanceRepository.findByIdAndUserId(transactionToCreate.bankId(), user.getId()).orElseThrow(()
                -> new EntityNotFoundException("Bank not found by id =" + transactionToCreate.bankId()));
        var categoryEntity =
                categoryRepository.findByIdAndUserId(transactionToCreate.categoryId(), user.getId()).orElseThrow(()
                        -> new EntityNotFoundException("Category not found by id =" + transactionToCreate.categoryId()));


        if (transactionToCreate.transactionType() == TransactionType.INCOME) {
            bankEntity.setBalance(bankEntity.getBalance() + transactionToCreate.amount().intValue());
        } else {
            if (bankEntity.getBalance() < transactionToCreate.amount()) {
                {
                    throw new IllegalStateException("Not enough money on bank balance");
                }
            }
            bankEntity.setBalance(bankEntity.getBalance() - transactionToCreate.amount().intValue());
        }
        balanceRepository.save(bankEntity);


        var entity = new TransactionEntity(
                null,
                categoryEntity,
                bankEntity,
                user,
                transactionToCreate.amount(),
                transactionToCreate.date(),
                LocalTime.now(),
                transactionToCreate.transactionType()

        );
        var savedTransaction = repository.save(entity);

        return mapper.toDtoCreateTransaction(savedTransaction);
    }

//    public Page<TransactionResponseDto> getAllTransactionsSortedByTime(Pageable pageable) {
//
//        UserEntity user = getCurrentUser();
//
//
//        return repository.findAllByUserIdOrderDateAndTime(user.getId(),pageable)
//                .map(mapper::toDtoTransactionResponse);
//    }


//    public List<TransactionResponseDto> getAllTransactionsByMonth(int month) {
//        UserEntity user = getCurrentUser();
//
//
//        if (month > 12 || month < 1) {
//            throw new IllegalArgumentException("The month was incorrect " + month);
//        }
//        return repository.findAllByMonthAndUserId(month, user.getId()).stream().map(mapper::toDtoTransactionResponse).toList();
//    }
//    public List<TransactionResponseDto> getAllTransactionsByCategory(Long categoryId) {
//        UserEntity user = getCurrentUser();
//
//
//        return repository.findAllByCategoryIdAndUserId(categoryId, user.getId()).stream().map(mapper::toDtoTransactionResponse).toList();
//    }

    @Transactional
    public void deleteTransaction(Long id) {
        UserEntity user = getCurrentUser();

        var transactionToDelete = repository.findByIdAndUserId(id, user.getId()).orElseThrow(() ->
                new EntityNotFoundException("transaction not found")
        );


        var bankEntity = transactionToDelete.getBank();


        if (transactionToDelete.getTransactionType() == TransactionType.INCOME) {
            bankEntity.setBalance(bankEntity.getBalance() - transactionToDelete.getAmountOfMoney().intValue());
        } else {
            bankEntity.setBalance(bankEntity.getBalance() + transactionToDelete.getAmountOfMoney().intValue());
        }
        balanceRepository.save(bankEntity);


        repository.deleteByIdAndUserId(id, user.getId());
    }

    @Transactional
    public CreateTransactionDto updateTransaction(Long id, CreateTransactionDto updatedTransaction) {

        UserEntity user = getCurrentUser();

        var categoryEntity =
                categoryRepository.findByIdAndUserId(updatedTransaction.categoryId(), user.getId()).orElseThrow(()
                        -> new EntityNotFoundException("Category not found by id =" + updatedTransaction.categoryId()));


        var oldTransaction = repository.findByIdAndUserId(id, user.getId())
                .orElseThrow(()
                        -> new EntityNotFoundException("Transaction not found by id =" + id));

        var oldBank = oldTransaction.getBank();
        var newBank = balanceRepository.findByIdAndUserId(updatedTransaction.bankId(), user.getId()).orElseThrow(()
                -> new EntityNotFoundException("Bank not found by id =" + updatedTransaction.bankId()));

        if (oldTransaction.getTransactionType() == TransactionType.INCOME) {
            oldBank.setBalance(oldBank.getBalance() - oldTransaction.getAmountOfMoney().intValue());
        } else {
            oldBank.setBalance(oldBank.getBalance() + oldTransaction.getAmountOfMoney().intValue());
        }

        if (updatedTransaction.transactionType() == TransactionType.INCOME) {
            newBank.setBalance(newBank.getBalance() + updatedTransaction.amount().intValue());
        } else if (updatedTransaction.transactionType() == TransactionType.EXPENSE) {
            if (newBank.getBalance() < updatedTransaction.amount().intValue()) {
                throw new IllegalStateException("Not enough money on bank balance");
            }
            newBank.setBalance(newBank.getBalance() - updatedTransaction.amount().intValue());
        }
        balanceRepository.save(oldBank);
        balanceRepository.save(newBank);

        var newTransaction = new TransactionEntity(
                oldTransaction.getId(),
                categoryEntity,
                newBank,
                user,
                updatedTransaction.amount(),
                updatedTransaction.date(),
                LocalTime.now(),
                updatedTransaction.transactionType()
        );

        var savedTransaction = repository.save(newTransaction);
        return mapper.toDtoCreateTransaction(savedTransaction);
    }

    public Page<TransactionResponseDto> getTransactions(
            Integer month,
            Long categoryId,
            Pageable pageable
    ) {

        UserEntity user = getCurrentUser();

        Page<TransactionEntity> transactions;

        if (month != null && categoryId != null) {

            transactions = repository
                    .findAllByUserIdAndMonthAndCategory(user.getId(), month, categoryId, pageable);

        } else if (month != null) {

            transactions = repository
                    .findAllByMonthAndUserId(month, user.getId(), pageable);

        } else if (categoryId != null) {

            transactions = repository
                    .findAllByCategoryIdAndUserId(categoryId, user.getId(), pageable);

        } else {

            transactions = repository
                    .findAllByUserId(user.getId(), pageable);
        }

        return transactions.map(mapper::toDtoTransactionResponse);
    }

    public StatisticsDto getStatistics(Integer month, Long categoryId, Long bankId) {
        UserEntity user = getCurrentUser();

        Long income = repository.getStatistics(user.getId(),
                TransactionType.INCOME,
                bankId, categoryId, month);

        Long expense = repository.getStatistics(user.getId(),
                TransactionType.EXPENSE,
                bankId, categoryId, month);

        return new StatisticsDto(
                income == null ? 0 : income,
                expense == null ? 0 : expense);
    }
}
