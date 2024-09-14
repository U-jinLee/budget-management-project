package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.market.MarketInterval;
import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.model.DivergenceType;
import com.example.budget.domain.trade.model.Signal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.ta4j.core.num.Num;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class OrderScheduler {

    @Value("${binance.value.limit}")
    private int limit;

    private final ImpulseSystem impulseSystem;
    private final DivergenceImpulseSystem divergenceImpulseSystem;
    private final OrderService orderService;
    private final MarketDataService marketDataService;

    @Scheduled(cron = "0 1 0,12 * * *")
    public void runAtMidnightAndNoon() {
        log.info("Running partial disposal take profit");
        orderService.partialDisposalTakeProfit();
    }

    /**
     * 10분마다 take profit and position에 대한 체크를 통한 전략을 생성
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void runEveryTenMinutes() {

        List<KlineDto> twelveHourlyMarketLines =
                marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);
        BarSeriesUtil halfTimeBarSeries = new BarSeriesUtil(twelveHourlyMarketLines);

        List<KlineDto> weekMarketLines =
                marketDataService.getFuturesMarketLines(MarketInterval.WEEKLY, true);
        BarSeriesUtil weekBarSeries = new BarSeriesUtil(weekMarketLines);

        /**
         * Impulse Check
         */
        Signal signal = impulseSystem
                .getSignal(weekBarSeries.ema(16).getSlope(), weekBarSeries.macd().getHistogramSlope());

        log.info("Signal :: {}", signal);

        if (orderService.isPositionExsits()) {
            /**
             * Divergence Impulse Check
             */
            log.info("Running take profit");
            signal = divergenceImpulseSystem
                    .checkDivergence(signal, halfTimeBarSeries, twelveHourlyMarketLines, DivergenceType.TAKE_PROFIT);

            orderService.takeProfit(twelveHourlyMarketLines);
        } else {
            /**
             * Divergence Impulse Check
             */
            log.info("Running order");
            signal = divergenceImpulseSystem
                    .checkDivergence(signal, halfTimeBarSeries, twelveHourlyMarketLines, DivergenceType.ORDER);

            orderService.order(signal);
        }

        log.info("Divergence Signal :: {}", signal);

    }

}