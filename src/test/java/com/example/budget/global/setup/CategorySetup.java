package com.example.budget.global.setup;

import com.example.budget.domain.category.entity.Category;
import com.example.budget.domain.category.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategorySetup {

    @Autowired
    CategoryRepository categoryRepository;

    public void save(int index) {
        for (int i = 0; i < index; i++) {
            categoryRepository.save(buildApplicant(i));
        }
    }

    private Category buildApplicant(int index) {
        return Category.builder()
                .name("test" + index)
                .description("Test description" + index)
                .build();
    }

}
