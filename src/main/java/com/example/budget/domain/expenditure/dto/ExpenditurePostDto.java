package com.example.budget.domain.expenditure.dto;

import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.expenditure.entity.Expenditure;
import com.example.budget.domain.expenditure.model.IsContain;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ExpenditurePostDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        @NotNull
        private Long amount;

        private String description;

        public static Request from(Long amount, String description) {
            return new Request(amount, description);
        }

        public Expenditure toEntity(Budget budget) {
            return Expenditure.builder()
                    .amount(this.amount)
                    .description(this.description)
                    .budget(budget)
                    .build();
        }

    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private Long id;
        private Long amount;
        private String description;
        private IsContain isContain;

        public static Response from(Expenditure expenditure) {
            return new Response(
                    expenditure.getId(),
                    expenditure.getAmount(),
                    expenditure.getDescription(),
                    expenditure.getIsContain());
        }
    }

}
