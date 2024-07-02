package com.example.budget.domain.investment.entity;

import com.example.budget.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class TradingRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "trading_reason", length = 255)
    private String tradingReason;

    @Column(name = "trading_date", nullable = false)
    private LocalDateTime tradingDate;

    @Column(name = "trading_price", nullable = false)
    private Float tradingPrice;

    @Column(name = "trading_quantity", nullable = false)
    private Float tradingQuantity;

    @Enumerated(value = EnumType.STRING)
    private Currency currency;

    @Enumerated(value = EnumType.STRING)
    private Position position;

    @Builder
    public TradingRecord(String name, String code, String tradingReason, LocalDateTime tradingDate,
                         Float tradingPrice, Float tradingQuantity, Currency currency, Position position) {
        this.name = name;
        this.code = code;
        this.tradingReason = tradingReason;
        this.tradingDate = tradingDate;
        this.tradingPrice = tradingPrice;
        this.tradingQuantity = tradingQuantity;
        this.currency = currency;
        this.position = position;
    }

    public String getTradingPrice() {
        return this.tradingPrice + this.getCurrency().getSymbol();
    }

}