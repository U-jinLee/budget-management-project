package com.example.budget.domain.trade.controller;

import com.example.budget.domain.trade.service.BybitAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Controller
public class TradeController {

    private final BybitAccountService bybitAccountService;

    @GetMapping("/")
    public String index(Model model) {
        BigDecimal balance = bybitAccountService.getUSDTAvailableBalance().getBalance()
                .setScale(4, RoundingMode.HALF_UP);
        model.addAttribute("balance", balance);
        return "index";
    }
}
