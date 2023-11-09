package com.example.budget.domain.client.repo;

import com.example.budget.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client, Long> {
}