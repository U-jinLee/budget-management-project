package com.example.budget.domain.budget.entity;

import com.example.budget.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "budget")
@Entity
public class Budget extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "amount_used", nullable = false)
    private Long amountUsed;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "email", nullable = false)
    private String email;

    @Builder
    public Budget(Long amount, String category, String email) {
        this.amount = amount;
        this.amountUsed = 0L;
        this.category = category;
        this.email = email;
    }

    public void updateAmount(Long amount) {
        this.amount = amount;
    }
}