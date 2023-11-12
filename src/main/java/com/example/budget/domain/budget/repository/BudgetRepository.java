package com.example.budget.domain.budget.repository;

import com.example.budget.domain.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    //해당 달의 선택한 유저의 특정 카테고리의 예산이 있는지 확인
    Optional<Budget> findByCreatedTimeBetweenAndEmailAndCategory(LocalDateTime start, LocalDateTime end, String email, String category);
    Optional<Budget> findByCreatedTimeBetweenAndId(LocalDateTime start, LocalDateTime end, Long id);

}
