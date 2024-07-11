package com.example.budget.domain.investment.repository;

import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.investment.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Page<Stock> findByClient(Client client, Pageable pageable);
}