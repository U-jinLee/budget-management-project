package com.example.budget.domain.client.service;

import com.example.budget.domain.client.dto.SignUpDto;
import com.example.budget.domain.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClientSignUpService {

    private final ClientRepository clientRepo;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpDto.Response signUp(SignUpDto.Request request) {
        return SignUpDto.Response.from(clientRepo.save(request.toEntity(passwordEncoder)));
    }
}
