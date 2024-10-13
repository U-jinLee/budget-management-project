package com.example.budget.domain.trade.model;

import com.example.budget.global.model.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Table(name = "take_profit")
@Entity
public class TakeProfit extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "realized_pnl")
    private BigDecimal realizedPnl;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "futures_order_id")
    private FuturesOrder futuresOrder;

    @Builder
    public TakeProfit(BigDecimal quantity, BigDecimal price, BigDecimal realizedPnl, FuturesOrder futuresOrder) {
        this.quantity = quantity;
        this.price = price;
        this.realizedPnl = realizedPnl;
        this.futuresOrder = futuresOrder;
    }

}