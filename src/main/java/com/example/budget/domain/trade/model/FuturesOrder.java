package com.example.budget.domain.trade.model;

import com.example.budget.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FuturesOrder extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Signal signal;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    FuturesOrder(Signal signal, Integer orderNumber, OrderStatus orderStatus) {
        this.signal = signal;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
    }

    public void reSigned() {
        this.orderStatus = OrderStatus.SIGNED;
    }
    
    public void finishOrder() {
        this.orderStatus = OrderStatus.DONE;
    }

    public void partialDisposeOrder() {
        this.orderStatus = OrderStatus.PARTIAL_DISPOSAL;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELED;
    }

}