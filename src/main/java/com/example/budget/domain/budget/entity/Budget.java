package com.example.budget.domain.budget.entity;

import com.example.budget.domain.category.entity.Category;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Table(name = "budget")
@Entity
public class Budget extends BaseTimeEntity {
    @Id
    private Long id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "amount_used", nullable = false)
    private Long amount_used;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Client user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}