package com.example.budget.domain.expenditure.repository;

import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.expenditure.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    Optional<Expenditure> findByBudgetAndId(Budget budget, Long id);
}
