package org.example.finance_tracker.entity;

import jakarta.persistence.*;

@Table(name = "banks")
@Entity
public class BanksEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "balance",nullable = false)
    private int balance;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    public BanksEntity() {
    }

    public BanksEntity(Long id, String name, int balance, UserEntity user) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
