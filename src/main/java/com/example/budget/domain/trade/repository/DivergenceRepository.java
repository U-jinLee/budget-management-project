package com.example.budget.domain.trade.repository;

import com.example.budget.domain.trade.model.Divergence;
import com.example.budget.domain.trade.model.DivergenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DivergenceRepository extends JpaRepository<Divergence, Long> {
    Optional<Divergence> findByDivergenceType(DivergenceType divergenceType);
}