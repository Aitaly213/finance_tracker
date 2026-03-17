package org.example.finance_tracker.entity;

import jakarta.persistence.*;

import java.util.List;

@Table(name = "category")
@Entity
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "emoji",nullable = false)
    private String emoji;

    @OneToMany(mappedBy = "category")
    private List<TransactionEntity> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;


    public CategoryEntity() {
    }

    public CategoryEntity(Long id, String title, String emoji, UserEntity user) {
        this.id = id;
        this.title = title;
        this.emoji = emoji;
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
