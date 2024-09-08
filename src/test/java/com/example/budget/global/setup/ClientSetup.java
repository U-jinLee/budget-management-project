package com.example.budget.global.setup;

import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.client.model.Role;
import com.example.budget.domain.client.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ClientSetup {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Client save() {
        return clientRepository.save(buildApplicant(0));
    }

    public void save(int index) {
        for (int i = 0; i < index; i++) {
            clientRepository.save(buildApplicant(i));
        }
    }

    private Client buildApplicant(int index) {
        return Client.testBuilder()
                .email("test@email.com")
                .password(passwordEncoder.encode("test"))
                .role(Role.USER)
                .build();
    }

}
