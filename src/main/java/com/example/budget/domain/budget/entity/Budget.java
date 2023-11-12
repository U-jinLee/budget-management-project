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
    @Builder(builderMethodName = "testBuilder", builderClassName = "testBuilder")
    public Budget(Long amount, Long amountUsed, String category, String email) {
        this.amount = amount;
        this.amountUsed = amountUsed;
        this.category = category;
        this.email = email;
    }

    public void updateAmount(Long amount) {
        this.amount = amount;
    }

    public void plusAmountUsed(Long amount) {
        this.amountUsed += amount;
    }

    public void minusAmountUsed(Long amount) {
        //사용 금액에 지출 금액 빼기
        this.amountUsed -= amount;
        //사용 금액이 음수면 사용 금액 0으로 초기화
        if(this.amountUsed < 0) this.amountUsed = 0L;
    }

}