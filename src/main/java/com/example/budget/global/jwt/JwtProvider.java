package com.example.budget.global.jwt;

import com.example.budget.domain.client.dto.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    private final Key key;
    private final long accessTokenValidMilliSecond;
    private final long refreshTokenValidMilliSecond;

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.access.token-validity-in-seconds}") long accessTokenValidMilliSecond,
                       @Value("${jwt.refresh.token-validity-in-seconds}") long refreshTokenValidMilliSecond) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidMilliSecond = accessTokenValidMilliSecond;
        this.refreshTokenValidMilliSecond = refreshTokenValidMilliSecond;

    }

    public TokenDto generateTokens(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + accessTokenValidMilliSecond);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date refreshTokenExpiresIn =
                new Date(now + refreshTokenValidMilliSecond);

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
