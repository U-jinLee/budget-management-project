package com.example.budget.domain.client.controller;

import com.example.budget.domain.client.dto.SignInDto;
import com.example.budget.domain.client.dto.SignUpDto;
import com.example.budget.domain.client.dto.TokenDto;
import com.example.budget.domain.client.service.ClientSignInService;
import com.example.budget.domain.client.service.ClientSignUpService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/authenticate")
@RestController
public class AuthenticateApiController {

    private final ClientSignUpService signUpService;
    private final ClientSignInService signInService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpDto.Response> signUp(@RequestBody @Valid SignUpDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpService.signUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenDto> signIn(@RequestBody @Valid SignInDto.Request request,
                                           HttpServletResponse servletResponse) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signInService.signIn(request, servletResponse));
    }

}