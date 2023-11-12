package com.example.budget.domain.client.controller;

import com.example.budget.domain.client.dto.SignUpDto;
import com.example.budget.domain.client.service.ClientSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/clients")
@RestController
public class ClientApiController {

    private final ClientSignUpService signUpService;
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpDto.Response> signUp(@RequestBody @Valid SignUpDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpService.signUp(request));
    }
}
