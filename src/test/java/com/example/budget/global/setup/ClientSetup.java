package com.example.budget.global.setup;

import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.client.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientSetup {

    @Autowired
    ClientRepository clientRepository;

    public Client save() {
        return clientRepository.save(buildApplicant(0));
    }

    public void save(int index) {
        for (int i = 0; i < index; i++) {
            clientRepository.save(buildApplicant(i));
        }
    }

    private Client buildApplicant(int index) {
        return Client.builder()
                .email("test@email.com")
                .password("test")
                .build();
    }

}
