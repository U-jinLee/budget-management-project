package com.example.budget.domain.trade.model;

import com.example.budget.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Table(name = "futures_order_transaction")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FuturesOrderTransaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "futures_order_id")
    private FuturesOrder futuresOrder;

}