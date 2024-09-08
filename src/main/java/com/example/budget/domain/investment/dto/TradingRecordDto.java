package com.example.budget.domain.investment.dto;

import com.example.budget.domain.investment.entity.Currency;
import com.example.budget.domain.investment.entity.Position;
import com.example.budget.domain.investment.entity.TradingRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class TradingRecordDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @NotEmpty
        private String name;

        private String code;

        private String tradingReason;

        @NotNull
        private LocalDateTime tradingDate;

        @NotNull
        private Float tradingPrice;

        @NotNull
        private Float tradingQuantity;

        private Position position;

        private Currency currency;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private long id;

        private String name;

        private String code;

        private String tradingReason;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime tradingDate;

        private String tradingPrice;

        private float tradingQuantity;

        private float totalPrice;

        private Position position;

        public Response (TradingRecord tradingRecord) {
            this.id = tradingRecord.getId();
            this.name = tradingRecord.getName();
            this.code = tradingRecord.getCode();
            this.tradingReason = tradingRecord.getTradingReason();
            this.tradingDate = tradingRecord.getTradingDate();
            this.tradingPrice = tradingRecord.getTradingPriceWithSymbol();
            this.tradingQuantity = tradingRecord.getTradingQuantity();
            this.totalPrice = tradingRecord.getTotalPrice();
            this.position = tradingRecord.getPosition();
        }
    }

}