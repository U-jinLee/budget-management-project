package com.example.budget.domain.category.service;

import com.example.budget.domain.category.dto.CategoryListDto;
import com.example.budget.domain.category.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryQueryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryListDto.Response> getCategories() {

        return categoryRepository.findAll()
                .stream().map(CategoryListDto.Response::from)
                .toList();

    }

}