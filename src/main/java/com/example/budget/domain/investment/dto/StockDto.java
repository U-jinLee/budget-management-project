package com.example.budget.domain.investment.dto;

import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.investment.entity.Stock;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class StockDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotEmpty
        private String name;
        private String code;
        @Positive
        private Float quantity;
        @Positive
        private Float annualDividend;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private long id;
        private String name;
        private String code;
        private float quantity;
        private float annualDividend;
        private Client client;

        public Response (Stock stock) {
            this.id = stock.getId();
            this.name = stock.getName();
            this.code = stock.getCode();
            this.quantity = stock.getQuantity();
            this.annualDividend = stock.getAnnualDividend();
            this.client = stock.getClient();
        }

    }

}