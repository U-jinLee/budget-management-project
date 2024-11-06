package com.example.budget.domain.trade.controller;

import com.example.budget.domain.trade.model.AccountInfoVo;
import com.example.budget.domain.trade.model.PositionVo;
import com.example.budget.domain.trade.repository.TakeProfitRepository;
import com.example.budget.domain.trade.service.BybitAccountService;
import com.example.budget.domain.trade.service.BybitPositionService;
import com.example.budget.domain.trade.service.BybitTradeService;
import com.example.budget.domain.trade.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Controller
public class TradeController {

    private final BybitPositionService bybitPositionService;
    private final BybitTradeService bybitTradeService;
    private final BybitAccountService bybitAccountService;
    private final MarketDataService marketDataService;
    private final TakeProfitRepository takeProfitRepository;

    @GetMapping("/")
    public String index(Model model) {
        BigDecimal markPrice = marketDataService.getMarkPrice();
        PositionVo positionInfo = bybitPositionService.getPositionInfo();
        AccountInfoVo accountInfo = bybitAccountService.getUSDTAvailableBalance();
        BigDecimal balance = accountInfo.getBalance().setScale(4, RoundingMode.HALF_UP);
        PositionVo openOrder = bybitTradeService.getOpenOrder();
        BigDecimal sevenDaysClosedPnL = bybitPositionService.getClosedPnL();

        model.addAttribute("markPrice", markPrice);
        model.addAttribute("isPositionExist", positionInfo.isExists());
        model.addAttribute("positionInfo", positionInfo);
        model.addAttribute("balance", balance);
        model.addAttribute("openOrder", openOrder);
        model.addAttribute("sevenDaysClosedPnL", sevenDaysClosedPnL);
        model.addAttribute("accountInfo", accountInfo);

        return "index";
    }
}
