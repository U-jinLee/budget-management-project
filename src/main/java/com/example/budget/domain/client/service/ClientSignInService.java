package com.example.budget.domain.client.service;

import com.example.budget.domain.client.dto.SignInDto;
import com.example.budget.domain.client.dto.TokenDto;
import com.example.budget.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClientSignInService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public TokenDto signIn(SignInDto.Request request, HttpServletResponse servletResponse) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        TokenDto tokenDto = jwtProvider.generateTokens(authentication);

        setHeader(servletResponse, tokenDto);

        return tokenDto;
    }


    private void setHeader(HttpServletResponse servletResponse, TokenDto tokenDto) {
        servletResponse.addHeader("Autorization", tokenDto.getGrantType()+" "+tokenDto.getAccessToken());
        servletResponse.addHeader("Refresh_Token", tokenDto.getRefreshToken());
    }
}