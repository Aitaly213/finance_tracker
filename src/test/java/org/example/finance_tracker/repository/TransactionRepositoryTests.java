package org.example.finance_tracker.repository;

import org.apache.commons.logging.Log;
import org.example.finance_tracker.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class TransactionRepositoryTests {

    private static final Logger log = LoggerFactory.getLogger(TransactionRepositoryTests.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity userEntity;
    private BanksEntity banksEntity;
    private CategoryEntity categoryEntity;

    @BeforeEach
    public void setUp(){
        userEntity = new UserEntity("John Doe","jondo@gmail.com","qwerty12345");
        entityManager.persist(userEntity);

        categoryEntity = new CategoryEntity(null,"Marketing","✔\uFE0F",userEntity);
        entityManager.persist(categoryEntity);

        banksEntity = new BanksEntity(null,"Demir",20000,userEntity);
        entityManager.persist(banksEntity);

        TransactionEntity t1 = new TransactionEntity(
                null,
                categoryEntity,
                banksEntity,
                userEntity,
                1000L,
                LocalDate.of(2026,3,1),
                LocalTime.of(10,0),
                TransactionType.EXPENSE
        );

        TransactionEntity t2 = new TransactionEntity(
                null,
                categoryEntity,
                banksEntity,
                userEntity,
                2000L,
                LocalDate.of(2026,3,2),
                LocalTime.of(11,0),
                TransactionType.EXPENSE
        );

        entityManager.persist(t1);
        entityManager.persist(t2);

        entityManager.flush();

    }

    @Test
    void findAllByUserId_shouldReturnTransactions() {

        List<TransactionEntity> transactions =
                transactionRepository.findAllByUserId(userEntity.getId());

        assertEquals(2, transactions.size());
    }

    @Test
    void findAllByMonthAndUserId_shouldReturnTransactionsForMonth() {

        List<TransactionEntity> transactions =
                transactionRepository.findAllByMonthAndUserId(3, userEntity.getId());

        assertEquals(2, transactions.size());
    }

    @Test
    void findAllByCategoryIdAndUserId_shouldReturnTransactions() {
        List<TransactionEntity> transactions =
                transactionRepository.findAllByCategoryIdAndUserId(
                        categoryEntity.getId(),
                        userEntity.getId()
                );

        assertEquals(2,transactions.size());

    }






























    @Test
    public void findAllByUserIdOrderDateAndTime_shouldReturnTransactionsSorted(){

        List<TransactionEntity> transactionEntities = transactionRepository.findAllByUserIdOrderDateAndTime(userEntity.getId());

        assertEquals(2,transactionEntities.size());

        assertTrue(
                transactionEntities.get(0).getDate().isAfter(transactionEntities.get(1).getDate())
        );

    }



}
