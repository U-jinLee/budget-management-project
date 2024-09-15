package com.example.budget.domain.trade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TradeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
