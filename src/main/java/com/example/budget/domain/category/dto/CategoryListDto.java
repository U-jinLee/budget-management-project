package com.example.budget.domain.category.dto;

import com.example.budget.domain.category.entity.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CategoryListDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        private Long id;
        private String name;
        private String description;

        public static Response from(Category category) {
            return new Response(
                    category.getId(),
                    category.getName(),
                    category.getDescription());
        }

    }

}
