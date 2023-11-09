package com.example.budget.domain.budget.service;

import com.example.budget.domain.budget.dto.BudgetPostDto;
import com.example.budget.domain.budget.exception.BudgetAlreadyExistsException;
import com.example.budget.domain.budget.repo.BudgetRepository;
import com.example.budget.domain.category.repo.CategoryRepository;
import com.example.budget.domain.client.repo.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class BudgetPostService {

    private final BudgetRepository budgetRepo;
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public BudgetPostDto.Response post(BudgetPostDto.Request request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate firstDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDateTime firstDateTime = LocalDateTime.of(firstDate, LocalTime.MIDNIGHT);

        int lastDay = firstDate.getMonth().maxLength();
        LocalDate lastDate = LocalDate.of(now.getYear(), now.getMonth(), lastDay);
        LocalDateTime lastDateTime = LocalDateTime.of(lastDate, LocalTime.MAX);


        if(!categoryRepository.existsByName(request.getCategory()) || !clientRepository.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("존재하지 않는 카테고리 or 이메일입니다.");

        if(budgetRepo.findByCreatedTimeBetweenAndEmailAndCategory(firstDateTime, lastDateTime, request.getEmail(), request.getCategory()).isPresent())
            throw new BudgetAlreadyExistsException();

        return BudgetPostDto.Response.from(budgetRepo.save(request.toEntity()));
    }

}
