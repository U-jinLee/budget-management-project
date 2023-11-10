package com.example.budget.domain.expenditure.dto;

import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.expenditure.entity.Expenditure;
import com.example.budget.domain.expenditure.model.IsContain;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenditureGetDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private Long id;
        private Long amount;
        private String description;
        private IsContain isContain;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;

        public static Response from(Expenditure expenditure) {
            return new Response(
                    expenditure.getId(),
                    expenditure.getAmount(),
                    expenditure.getDescription(),
                    expenditure.getIsContain(),
                    expenditure.getCreatedTime());
        }
    }

}
