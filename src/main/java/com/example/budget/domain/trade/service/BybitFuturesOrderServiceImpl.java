package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.market.MarketInterval;
import com.bybit.api.client.domain.position.TpslMode;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import com.example.budget.domain.trade.dto.*;
import com.example.budget.domain.trade.exception.OrderNotFoundException;
import com.example.budget.domain.trade.exception.SignalUnknownException;
import com.example.budget.domain.trade.model.*;
import com.example.budget.domain.trade.repository.FuturesOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.num.DecimalNum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BybitFuturesOrderServiceImpl implements OrderService {

    private static final BigDecimal POSITION_SIZE = BigDecimal.valueOf(0.125);
    private final BybitApiClientFactory bybitApiClientFactory;
    private final FuturesOrderRepository futuresOrderRepository;
    private final MarketDataService marketDataService;
    private final BybitAccountService bybitAccountService;
    private final BybitTradeService bybitTradeService;

    @Override
    @Transactional
    public void execute(Signal signal) {

        if (Signal.UNKNOWN.equals(signal)) throw new SignalUnknownException();

        /**
         * If there is a position with a reservation at stake, delete the existing position and enter a new position
         */
        if (isOutstandingOrderExist()) {
            bybitTradeService.cancelAllOrder();
            futuresOrderRepository.findByOrderStatus(OrderStatus.SIGNED)
                    .orElseThrow(OrderNotFoundException::new).cancelOrder();
        } else {
            futuresOrderRepository.findByOrderStatus(OrderStatus.SIGNED).ifPresent(o -> {
                o.cancelOrder();
                futuresOrderRepository.saveAndFlush(o);
            });
        }
        List<KlineDto> klines =
                this.marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);

        BarSeriesUtil barSeries = new BarSeriesUtil(klines);
        BollingerBandDto bollingerBand = barSeries.bollingerBand(50, 2.1);
        RsiDto rsi = barSeries.rsi();
        MaDto sma = barSeries.sma(16);

        RsiDto divergenceRsi = barSeries.rsi(175, 198);
        MinAndMaxDto divergenceClosePrice = barSeries.closePrice(175, 198);

        Collections.reverse(klines);
        BigDecimal markPrice = marketDataService.getMarkPrice();

        String buyStopLoss = markPrice.multiply(BigDecimal.ONE.subtract(BybitAttributes.STOP_LOSS_PERCENTAGE))
                .setScale(2, RoundingMode.HALF_UP).toString();

        String sellStopLoss = markPrice.multiply(BigDecimal.ONE.add(BybitAttributes.STOP_LOSS_PERCENTAGE))
                .setScale(2, RoundingMode.HALF_UP).toString();

        if (Signal.GREEN.equals(signal)) {
            /**
             * 1번 째 조건
             */
            if (sma.getValue().isLessThan(DecimalNum.valueOf(klines.get(1).getClosePrice())) &&
                    sma.getValue().isLessThan(DecimalNum.valueOf(markPrice))) {

                BigDecimal price = BigDecimal.valueOf(sma.getValue().doubleValue())
                        .setScale(2, RoundingMode.HALF_UP);
                newOrder(Side.BUY, price.toString(), buyStopLoss, signal, 1);

            }

            /**
             * 2번 째 조건
             */
            BigDecimal compareValue =
                    BigDecimal.valueOf(sma.getValue().multipliedBy(DecimalNum.valueOf(0.95)).doubleValue());
            BigDecimal middleBand = BigDecimal.valueOf(bollingerBand.getMiddleBand().doubleValue());

            if (markPrice.compareTo(compareValue) < 0 && markPrice.compareTo(middleBand) > 0) {
                BigDecimal price = BigDecimal.valueOf(bollingerBand.getMiddleBand().doubleValue())
                        .setScale(2, RoundingMode.HALF_UP);
                newOrder(Side.BUY, price.toString(), buyStopLoss, signal, 2);
            }

        }

        if (Signal.YELLOW.equals(signal)) {
            if (markPrice.compareTo(BigDecimal.valueOf(bollingerBand.getLowerBand().doubleValue())) <= 0 &&
                    rsi.getValue().isLessThanOrEqual(DecimalNum.valueOf(30))) {
                BigDecimal previousClosePrice = klines.get(1).getClosePrice();
                newOrder(Side.BUY, previousClosePrice.toString(), buyStopLoss, signal, 1);
            }

            if (markPrice.compareTo(BigDecimal.valueOf(bollingerBand.getUpperBand().doubleValue())) >= 0 &&
                    rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(70))) {
                BigDecimal previousClosePrice = klines.get(1).getClosePrice();
                newOrder(Side.SELL, previousClosePrice.toString(), sellStopLoss, signal, 2);
            }

            //RSI Divergenced 시
            if (divergenceClosePrice.getMin().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) &&
                    divergenceRsi.getMin().isLessThanOrEqual(rsi.getValue())) {
                BigDecimal price = marketDataService.getMarkPrice();
                newOrder(Side.BUY, price.toString(), buyStopLoss, signal, 3);
            }

            //RSI Divergenced 시
            if (divergenceClosePrice.getMax().isLessThanOrEqual(DecimalNum.valueOf(markPrice))
                    && divergenceRsi.getMax().isGreaterThanOrEqual(rsi.getValue())) {
                BigDecimal price = marketDataService.getMarkPrice();
                newOrder(Side.SELL, price.toString(), sellStopLoss, signal, 4);
            }

        }

        if (Signal.RED.equals(signal)) {
            BigDecimal previousClosePrice = klines.get(1).getClosePrice();

            /**
             * 1번 째 조건
             */
            if (sma.getValue().isGreaterThan(DecimalNum.valueOf(previousClosePrice)) &&
                    sma.getValue().isGreaterThan(DecimalNum.valueOf(markPrice))) {
                BigDecimal price = BigDecimal.valueOf(sma.getValue().doubleValue())
                        .setScale(2, RoundingMode.HALF_UP);
                newOrder(Side.SELL, price.toString(), sellStopLoss, signal, 1);
            }

            /**
             * 2번 째 조건
             */
            BigDecimal compareValue =
                    BigDecimal.valueOf(sma.getValue().multipliedBy(DecimalNum.valueOf(1.05)).doubleValue());

            BigDecimal middleBand = BigDecimal.valueOf(bollingerBand.getMiddleBand().doubleValue());

            if (markPrice.compareTo(compareValue) > 0 && markPrice.compareTo(middleBand) < 0) {
                BigDecimal price = BigDecimal.valueOf(bollingerBand.getMiddleBand().doubleValue())
                        .setScale(2, RoundingMode.HALF_UP);
                newOrder(Side.SELL, price.toString(), sellStopLoss, signal, 2);
            }

        }

    }

    private void newOrder(Side side, String price, String stopLoss, Signal signal, int orderNumber) {
        BybitApiTradeRestClient tradeClient = bybitApiClientFactory.newTradeRestClient();

        String orderQty = bybitAccountService.getUSDTAvailableBalance()
                .calculateOrderQuantity(POSITION_SIZE, marketDataService.getMarkPrice()).toString();

        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .side(side)
                .orderType(TradeOrderType.LIMIT)
                .price(price)
                .tpslMode(TpslMode.PARTIAL.getDescription())
                .stopLoss(stopLoss)
                .qty(orderQty)
                .build();

        log.info(tradeClient.createOrder(request).toString());

        futuresOrderRepository.save(
                FuturesOrder.builder()
                        .orderSignal(signal)
                        .orderNumber(orderNumber)
                        .orderStatus(OrderStatus.SIGNED)
                        .build());
    }

    private boolean isOutstandingOrderExist() {
        BybitApiTradeRestClient tradeClient = bybitApiClientFactory.newTradeRestClient();
        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .openOnly(0)
                .build();

        boolean result = false;

        try {
            String jsonString = new ObjectMapper().writeValueAsString(tradeClient.getOpenOrders(request));
            JsonArray jsonArray = new Gson().fromJson(jsonString, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list");

            if (jsonArray.size() != 0) result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}