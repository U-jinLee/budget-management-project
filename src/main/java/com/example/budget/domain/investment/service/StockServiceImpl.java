package com.example.budget.domain.investment.service;

import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.client.repository.ClientRepository;
import com.example.budget.domain.investment.dto.StockDto;
import com.example.budget.domain.investment.entity.Stock;
import com.example.budget.domain.investment.repository.StockRepository;
import com.example.budget.global.jwt.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService {

    private final ClientRepository clientRepository;
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final JwtUtil jwtUtil;

    @Override
    public Page<Stock> getStocks(HttpServletRequest servletRequest, Pageable pageable) {
        String accessToken = servletRequest.getHeader("Authorization").substring(7);

        String email = jwtUtil.parseClaims(accessToken).getSubject();

        Client client = clientRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        return stockRepository.findByClient(client, pageable);
    }

    @Override
    public Stock post(StockDto.Request request, HttpServletRequest servletRequest) {
        String accessToken = servletRequest.getHeader("Authorization").substring(7);

        String email = jwtUtil.parseClaims(accessToken).getSubject();
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        return stockRepository.save(stockMapper.toEntity(request, client));
    }

}
