package com.example.budget.domain.client.service;

import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.client.exception.ClientNotFoundException;
import com.example.budget.domain.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clientRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(ClientNotFoundException::new);
    }

    private UserDetails createUserDetails(Client client) {
        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword())
                .roles(client.getRole().toString())
                .build();
    }
}
