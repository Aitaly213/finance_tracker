package org.example.finance_tracker.repository;

import org.example.finance_tracker.entity.TransactionEntity;
import org.example.finance_tracker.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("select t from TransactionEntity t where t.user.id = :userId order by t.date DESC, t.time DESC")
    Page<TransactionEntity> findAllByUserIdOrderDateAndTime(Long userId, Pageable pageable);

    @Query("select t from TransactionEntity t where t.user.id = :userId and month(t.date) = :month ")
    Page<TransactionEntity> findAllByMonthAndUserId(int month, Long userId, Pageable pageable);

    Page<TransactionEntity> findAllByCategoryIdAndUserId(Long categoryId, Long userId, Pageable pageable);

    Page<TransactionEntity> findAllByUserId(Long userId, Pageable pageable);

    void deleteByIdAndUserId(Long id, Long userId);

    Optional<TransactionEntity> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT t FROM TransactionEntity t
            WHERE t.user.id = :userId
            AND MONTH(t.date) = :month
            AND t.category.id = :categoryId
            """)
    Page<TransactionEntity> findAllByUserIdAndMonthAndCategory(
            Long userId,
            int month,
            Long categoryId,
            Pageable pageable
    );

    @Query("""
            select sum(t.amountOfMoney) from TransactionEntity t
                        where t.user.id = :userId
                        and t.transactionType = :transactionType
                        and (:bankId is null or t.bank.id = :bankId)
                        and (:categoryId is null or t.category.id = :categoryId)
                        and (:month is null or month(t.date) = :month)
            """)
    Long getStatistics(Long userId, TransactionType transactionType, Long bankId, Long categoryId, Integer month);
}
