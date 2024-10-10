package com.example.budget.domain.trade.controller;

import com.example.budget.domain.trade.model.PositionVo;
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

    @GetMapping("/")
    public String index(Model model) {
        BigDecimal markPrice = marketDataService.getMarkPrice();
        PositionVo positionInfo = bybitPositionService.getPositionInfo();
        BigDecimal balance = bybitAccountService.getUSDTAvailableBalance().getBalance()
                .setScale(4, RoundingMode.HALF_UP);
        PositionVo openOrder = bybitTradeService.getOpenOrder();
        BigDecimal sevenDaysClosedPnL = bybitPositionService.getClosedPnL();

        model.addAttribute("markPrice", markPrice);
        model.addAttribute("isPositionExist", positionInfo.isExists());
        model.addAttribute("positionInfo", positionInfo);
        model.addAttribute("balance", balance);
        model.addAttribute("openOrder", openOrder);
        model.addAttribute("sevenDaysClosedPnL", sevenDaysClosedPnL);
        return "index";
    }
}
