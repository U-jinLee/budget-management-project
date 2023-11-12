package com.example.budget.domain.client.dto;

import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.client.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SignUpDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        @Email
        @NotEmpty
        private String email;

        @NotEmpty
        private String password;

        public static SignUpDto.Request from(String email, String password) {
            return new SignUpDto.Request(
                    email,
                    password);
        }

        public Client toEntity(PasswordEncoder encoder) {
            return Client.builder()
                    .email(email)
                    .password(encoder.encode(password))
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private long id;
        private String email;
        private Role role;

        public static Response from(Client client) {
            return new Response(
                    client.getId(),
                    client.getEmail(),
                    client.getRole());
        }

    }
}
