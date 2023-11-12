package com.example.budget.domain.expenditure.entity;

import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.expenditure.dto.ExpenditurePutDto;
import com.example.budget.domain.expenditure.model.IsContain;
import com.example.budget.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Expenditure extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "description", length = 50)
    private String description;

    @Enumerated(EnumType.STRING)
    private IsContain isContain;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @Builder
    public Expenditure(Long amount, String description, Budget budget) {
        this.amount = amount;
        this.description = description;
        this.isContain = IsContain.CONTAIN;
        this.budget = budget;
    }

    public void changeContainStatus() {
        if(this.isContain.equals(IsContain.CONTAIN))
            this.isContain = IsContain.NOT_CONTAIN;
    }

    public void updateTo(ExpenditurePutDto.Request request) {
        this.amount = request.getAmount() == null ? this.amount : request.getAmount();
        this.description = request.getDescription() == null ? this.description : request.getDescription();
    }

}