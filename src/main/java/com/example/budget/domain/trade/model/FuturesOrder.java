package com.example.budget.domain.trade.model;

import com.example.budget.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "futures_order")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FuturesOrder extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_signal")
    private Signal orderSignal;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    FuturesOrder(Signal orderSignal, Integer orderNumber, OrderStatus orderStatus) {
        this.orderSignal = orderSignal;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
    }

    public boolean isPositionActive() {
        return this.orderStatus.equals(OrderStatus.SIGNED) || this.orderStatus.equals(OrderStatus.PARTIAL_DISPOSAL);
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

    public void liquidatedPosition() {
        this.orderStatus = OrderStatus.LIQUIDATED;
    }

}