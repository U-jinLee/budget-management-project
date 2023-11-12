package com.example.budget.domain.expenditure.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ExpenditurePutDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        private Long amount;
        private String description;

        public static Request from(Long amount, String description) {
            return new Request(amount, description);
        }

    }

}
