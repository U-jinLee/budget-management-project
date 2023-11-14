package com.example.budget.domain.budget.service;

import com.example.budget.domain.budget.dto.BudgetPostDto;
import com.example.budget.domain.budget.exception.BudgetAlreadyExistsException;
import com.example.budget.domain.budget.repository.BudgetRepository;
import com.example.budget.domain.category.repo.CategoryRepository;
import com.example.budget.domain.client.repository.ClientRepository;
import com.example.budget.domain.expenditure.exception.InvalidRequestBudgetFieldsException;
import com.example.budget.global.jwt.JwtUtil;
import com.example.budget.global.util.ThisMonth;
import com.example.budget.global.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BudgetPostService {

    private final BudgetRepository budgetRepo;
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public BudgetPostDto.Response post(BudgetPostDto.Request request,
                                       HttpServletRequest servletRequest) {

        // HttpServlet Request에서 토큰을 가져와서 이메일을 추출
        String email = getUserEmailFrom(servletRequest);

        // 요청 필드 검증
        if(validateRequestFields(request, email))
            throw new InvalidRequestBudgetFieldsException();

        // 이미 존재하는 예산인지 검증
        if(alreadyHasBudget(request, email))
            throw new BudgetAlreadyExistsException();

        return BudgetPostDto.Response.from(budgetRepo.save(request.toEntity(email)));
    }

    private String getUserEmailFrom(HttpServletRequest servletRequest) {
        String authorization = servletRequest.getHeader("Authorization");

        return jwtUtil.parseClaims(authorization.substring(7))
                .getSubject();
    }

    private boolean validateRequestFields(BudgetPostDto.Request request, String email) {
        return !categoryRepository.existsByName(request.getCategory()) || !clientRepository.existsByEmail(email);
    }

    private boolean alreadyHasBudget(BudgetPostDto.Request request, String email) {

        ThisMonth thisMonth = TimeUtil.getThisMonth();

        return budgetRepo.findByCreatedTimeBetweenAndEmailAndCategory(
                thisMonth.getStartDateTime(),
                thisMonth.getEndDateTime(),
                email,
                request.getCategory()).isPresent();

    }

}
