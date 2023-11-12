package com.example.budget.global.setup;

import com.example.budget.domain.category.entity.Category;
import com.example.budget.domain.category.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategorySetup {

    @Autowired
    CategoryRepository categoryRepository;

    public Category save() {
        return categoryRepository.save(buildApplicant(0));
    }

    public Category save(String name) {
        return categoryRepository.save(buildApplicant(name));
    }


    public List<Category> save(int index) {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            categories.add(categoryRepository.save(buildApplicant(i)));
        }
        return categories;
    }

    private Category buildApplicant(int index) {
        return Category.builder()
                .name("test" + index)
                .description("Test description" + index)
                .build();
    }

    private Category buildApplicant(String name) {
        return Category.builder()
                .name(name)
                .description("Test description")
                .build();
    }
}
