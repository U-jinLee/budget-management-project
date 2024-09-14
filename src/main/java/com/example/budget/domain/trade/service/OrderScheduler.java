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
        List<KlineDto> halfTimeKlines =
                orderService.getFuturesHistoricalKlines(MarketInterval.TWELVE_HOURLY, this.limit, true);
        BarSeriesUtil halfTimeBarSeries = new BarSeriesUtil(halfTimeKlines);

        List<KlineDto> weekKlines =
                orderService.getFuturesHistoricalKlines(MarketInterval.WEEKLY, this.limit, true);
        BarSeriesUtil weekBarSeries = new BarSeriesUtil(weekKlines);

        /**
         * Impulse Check
         */
        Num weekEmaSlope = weekBarSeries.ema(16).getSlope();
        Num histogramSlope = weekBarSeries.macd().getHistogramSlope();
        Signal signal = impulseSystem.getSignal(weekEmaSlope, histogramSlope);

        log.info("Signal :: {}", signal);

        if (orderService.isPositionExsits()) {
            /**
             * Divergence Impulse Check
             */
            log.info("Running take profit");
            signal = divergenceImpulseSystem
                    .checkDivergence(signal, halfTimeBarSeries, halfTimeKlines, DivergenceType.TAKE_PROFIT);

            orderService.takeProfit(halfTimeKlines);
        } else {
            /**
             * Divergence Impulse Check
             */
            log.info("Running order");
            signal = divergenceImpulseSystem
                    .checkDivergence(signal, halfTimeBarSeries, halfTimeKlines, DivergenceType.ORDER);

            orderService.order(signal);
        }

        log.info("Divergence Signal :: {}", signal);

    }

}