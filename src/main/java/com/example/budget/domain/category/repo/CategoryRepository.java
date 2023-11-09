package com.example.budget.domain.category.repo;

import com.example.budget.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}