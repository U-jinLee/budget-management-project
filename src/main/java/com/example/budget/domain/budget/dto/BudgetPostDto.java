package com.example.budget.domain.budget.dto;

import com.example.budget.domain.budget.entity.Budget;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BudgetPostDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        @NotNull
        private Long amount;

        @Email
        @NotEmpty
        private String email;

        @NotEmpty
        private String category;

        public static Request from(Long amount,String email, String category) {
            return new Request(
                    amount,
                    email,
                    category);
        }

        public Budget toEntity() {
            return Budget.builder()
                    .amount(amount)
                    .email(email)
                    .category(category)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private Long id;
        private Long amount;
        private Long amountUsed;
        private String category;
        private String email;

        public static Response from(Budget budget) {
            return new Response(
                    budget.getId(),
                    budget.getAmount(),
                    budget.getAmountUsed(),
                    budget.getCategory(),
                    budget.getEmail());
        }

    }

}
