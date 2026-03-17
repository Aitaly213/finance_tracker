package org.example.finance_tracker.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "transactions")
@Entity
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "bank_id",nullable = false)
    private BanksEntity bank;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @Column(name = "amount",nullable = false)
    private Long amountOfMoney;

    @Column(name = "date",nullable = false)
    private LocalDate date;

    @Column(name = "time",nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type",nullable = false)
    private TransactionType transactionType;


    public TransactionEntity() {
    }

    public TransactionEntity(Long id, CategoryEntity category, BanksEntity bank, UserEntity user, Long amountOfMoney, LocalDate date, LocalTime time, TransactionType transactionType) {
        this.id = id;
        this.category = category;
        this.bank = bank;
        this.user = user;
        this.amountOfMoney = amountOfMoney;
        this.date = date;
        this.time = time;
        this.transactionType = transactionType;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BanksEntity getBank() {
        return bank;
    }

    public void setBank(BanksEntity bank) {
        this.bank = bank;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Long getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(Long amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
