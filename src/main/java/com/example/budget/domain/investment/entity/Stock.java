package com.example.budget.domain.investment.entity;

import com.example.budget.domain.client.entity.Client;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "annual_dividend")
    private Float annualDividend;

    @Column(name = "total_annual_dividend")
    private Float totalAnnualDividend;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Builder
    public Stock(long id,
                 String name,
                 String code,
                 float quantity,
                 float annualDividend,
                 Client client) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.annualDividend = annualDividend;
        this.client = client;
        this.totalAnnualDividend = annualDividend * quantity;
    }

}