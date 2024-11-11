package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.market.MarketInterval;
import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.model.DivergenceType;
import com.example.budget.domain.trade.model.Signal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class OrderScheduler {

    private final ImpulseSystem impulseSystem;
    private final DivergenceImpulseSystem divergenceImpulseSystem;
    private final TakeProfitService takeProfitService;
    private final OrderService orderService;
    private final MarketDataService marketDataService;
    private final BybitPositionService bybitPositionService;

    @Scheduled(cron = "0 1 9,21 * * *")
    public void runAtMidnightAndNoon() {
//        List<KlineDto> twelveHourlyMarketLines =
//                marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);
//        BarSeriesUtil halfTimeBarSeries = new BarSeriesUtil(twelveHourlyMarketLines);
//
//        List<KlineDto> weekMarketLines =
//                marketDataService.getFuturesMarketLines(MarketInterval.WEEKLY, true);
//        BarSeriesUtil weekBarSeries = new BarSeriesUtil(weekMarketLines);
//
//        /**
//         * Impulse Check
//         */
//        Signal signal = impulseSystem
//                .getSignal(weekBarSeries.ema(16).getSlope(), weekBarSeries.macd().getHistogramSlope());
//
//        signal = divergenceImpulseSystem
//                .checkDivergence(signal, halfTimeBarSeries, twelveHourlyMarketLines, DivergenceType.TAKE_PROFIT);
//
//        takeProfitService.execute(signal);
    }

    /**
     * 10분마다 새로운 주문을 생성, 포지션이 존재할 시 주문을 만들지 않음
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void runEveryTenMinutes() {
//
//        List<KlineDto> twelveHourlyMarketLines =
//                marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);
//        BarSeriesUtil halfTimeBarSeries = new BarSeriesUtil(twelveHourlyMarketLines);
//
//        List<KlineDto> weekMarketLines =
//                marketDataService.getFuturesMarketLines(MarketInterval.WEEKLY, true);
//        BarSeriesUtil weekBarSeries = new BarSeriesUtil(weekMarketLines);
//
//        /**
//         * Impulse Check
//         */
//        Signal signal = impulseSystem
//                .getSignal(weekBarSeries.ema(16).getSlope(), weekBarSeries.macd().getHistogramSlope());
//
//        if (!bybitPositionService.getPositionInfo().isExists()) {
//
//            signal = divergenceImpulseSystem
//                    .checkDivergence(signal, halfTimeBarSeries, twelveHourlyMarketLines, DivergenceType.ORDER);
//
//            orderService.execute(signal);
//        }

    }

}