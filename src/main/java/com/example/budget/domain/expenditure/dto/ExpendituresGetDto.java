package com.example.budget.domain.expenditure.dto;

import com.example.budget.domain.expenditure.entity.Expenditure;
import com.example.budget.domain.expenditure.model.IsContain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class ExpendituresGetDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private long id;
        private long amount;
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
