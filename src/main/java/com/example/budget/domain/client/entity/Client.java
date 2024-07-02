package com.example.budget.domain.client.entity;

import com.example.budget.domain.client.model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "client")
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 400)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Builder
    public Client(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }
}