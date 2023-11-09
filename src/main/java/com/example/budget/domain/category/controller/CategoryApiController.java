package com.example.budget.domain.category.controller;

import com.example.budget.domain.category.dto.CategoryListDto;
import com.example.budget.domain.category.service.CategoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/categories")
@RequiredArgsConstructor
@RestController
public class CategoryApiController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping
    public ResponseEntity<List<CategoryListDto.Response>> getCategories() {
        return ResponseEntity.ok().body(categoryQueryService.getCategories());
    }

}