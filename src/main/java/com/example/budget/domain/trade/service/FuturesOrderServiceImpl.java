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
import com.example.budget.domain.trade.exception.PositionIsLiquidatedException;
import com.example.budget.domain.trade.exception.SignalUnknownException;
import com.example.budget.domain.trade.model.*;
import com.example.budget.domain.trade.repository.DivergenceRepository;
import com.example.budget.domain.trade.repository.FuturesOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.num.DecimalNum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuturesOrderServiceImpl implements OrderService {

    @Value("${bybit.key.test-api}")
    private String apiKey;

    @Value("${bybit.key.test-secret}")
    private String secretKey;

    private static final BigDecimal POSITION_SIZE = BigDecimal.valueOf(0.125);

    private final BybitApiClientFactory bybitApiClientFactory;
    private final FuturesOrderRepository futuresOrderRepository;
    private final DivergenceRepository divergenceRepository;
    private final MarketDataService marketDataService;
    private final BybitPositionService bybitPositionService;
    private final BybitAccountService bybitAccountService;
    private final BybitTradeService bybitTradeService;

    @Override
    @Transactional
    public void partialDisposalTakeProfit(Signal signal) {
        List<OrderStatus> orderStatuses = new ArrayList<>();
        orderStatuses.add(OrderStatus.SIGNED);
        orderStatuses.add(OrderStatus.PARTIAL_DISPOSAL);

        FuturesOrder futuresOrder = futuresOrderRepository.findByOrderStatusIn(orderStatuses)
                .orElseThrow(OrderNotFoundException::new);

        PositionVo positionInfo = bybitPositionService.getPositionInfo();
        BigDecimal markPrice = marketDataService.getMarkPrice();

        if (isPositionLiquidated(positionInfo, futuresOrder)) {
            futuresOrder.liquidatedPosition();
            throw new PositionIsLiquidatedException();
        }

        List<KlineDto> klines =
                marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);

        BarSeriesUtil barSeries = new BarSeriesUtil(klines);

        BollingerBandDto bollingerBand = barSeries.bollingerBand(50, 2.1);
        RsiDto rsi = barSeries.rsi();

        Collections.reverse(klines);

        BigDecimal minimumQty = bybitAccountService.getUSDTAvailableBalance()
                .calculateOrderQuantity(BigDecimal.valueOf(0.065), marketDataService.getMarkPrice());

        Optional<Divergence> divergence = divergenceRepository.findByDivergenceType(DivergenceType.TAKE_PROFIT);
        /**
         * Green, Yellow, Red 모두 Divergence에 대한 조건을 정리한다
         * 1. Signed, Take Profit 두 상황 모두 정리
         * 2. 반반씩 정리하는 로직은 동일
         *  a. 일정 수량 아래로 내려왔을 시 모두 정리
         */
        //녹색:
        if (Signal.GREEN.equals(futuresOrder.getOrderSignal())) {
            // 녹색 1-1: 전전봉 마감이 전봉 마감보다 높을 때 익절,
            // rsi가 75 이상,
            // 포지션의 수익률이 36%(레버리지 3배 기준),
            // 볼린저 밴드 상단에 닿을 때
            if (futuresOrder.getOrderNumber().equals(1)) {
                if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(36)) >= 0 ||
                        klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) > 0 ||
                        rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(75)) ||
                        bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice))) {

                    if (!positionInfo.sizeIsBiggerThan(minimumQty)) {
                        bybitTradeService
                                .takeProfit(Side.SELL, markPrice.toString(), positionInfo.getSize().toString());
                        futuresOrder.finishOrder();
                    }

                    divergence.ifPresent(d -> {
                        if (d.getFormerSignal().equals(Signal.GREEN) &&
                                futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                            String halfPositionSize =
                                    bybitPositionService
                                            .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();

                            bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), halfPositionSize);

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        String halfPositionSize =
                                bybitPositionService
                                        .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();

                        bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), halfPositionSize);

                        futuresOrder.partialDisposeOrder();
                    }

                }

            }

            // 녹색 1-2: 전전봉 마감이 전봉 마감보다 높을 때 익절,
            // rsi가 75 이상,
            // 포지션의 수익률이 45%(레버리지 3배 기준),
            // 볼린저 밴드 상단에 닿을 때,
            if (futuresOrder.getOrderNumber().equals(2)) {
                if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(45)) >= 0 ||
                        klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) > 0 ||
                        rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(75)) ||
                        bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice))) {

                    if (!positionInfo.sizeIsBiggerThan(minimumQty)) {
                        bybitTradeService
                                .takeProfit(Side.SELL, markPrice.toString(), positionInfo.getSize().toString());
                        futuresOrder.finishOrder();
                    }

                    divergence.ifPresent(d -> {
                        if (d.getFormerSignal().equals(Signal.GREEN) &&
                                futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                            String halfPositionSize =
                                    bybitPositionService
                                            .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();

                            bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), halfPositionSize);

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        String halfPositionSize =
                                bybitPositionService
                                        .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();
                        bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), halfPositionSize);
                        futuresOrder.partialDisposeOrder();
                    }

                }

            }

        }

        //노란색:
        if (Signal.YELLOW.equals(futuresOrder.getOrderSignal())) {
            // 노란색 1-1: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 상단선 도달, rsi 70 이상
            if (futuresOrder.getOrderNumber().equals(1)) {
                if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                        bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                        bollingerBand.getMiddleBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                        rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(70))) {

                    if (!positionInfo.sizeIsBiggerThan(minimumQty)) {
                        bybitTradeService
                                .takeProfit(Side.SELL, markPrice.toString(), positionInfo.getSize().toString());
                        futuresOrder.finishOrder();
                    }


                    divergence.ifPresent(d -> {
                        if (d.getFormerSignal().equals(Signal.GREEN) &&
                                futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                            String halfPositionSize =
                                    bybitPositionService
                                            .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();

                            bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), halfPositionSize);

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        String halfPositionSize =
                                bybitPositionService
                                        .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();
                        bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), halfPositionSize);
                        futuresOrder.partialDisposeOrder();
                    }

                }

            }
            // 노란색 1-2: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 하단선 도달, rsi 30 이하
            if (futuresOrder.getOrderNumber().equals(2)) {
                if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                        bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                        bollingerBand.getMiddleBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                        rsi.getValue().isLessThanOrEqual(DecimalNum.valueOf(30))) {

                    if (!positionInfo.sizeIsBiggerThan(minimumQty)) {
                        bybitTradeService
                                .takeProfit(Side.BUY, markPrice.toString(), positionInfo.getSize().toString());
                        futuresOrder.finishOrder();
                    }

                    divergence.ifPresent(d -> {
                        if (d.getFormerSignal().equals(Signal.RED) &&
                                futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                            String halfPositionSize =
                                    bybitPositionService
                                            .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();

                            bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), halfPositionSize);

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        String halfPositionSize =
                                bybitPositionService
                                        .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();
                        bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), halfPositionSize);
                        futuresOrder.partialDisposeOrder();
                    }

                }
            }
            // 노란색 1-3: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 상단선 도달, rsi 70 이상
            if (futuresOrder.getOrderNumber().equals(3)) {
                if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                        bollingerBand.getMiddleBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                        bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                        rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(70))) {

                    if (!positionInfo.sizeIsBiggerThan(minimumQty)) {
                        bybitTradeService
                                .takeProfit(Side.SELL, markPrice.toString(), positionInfo.getSize().toString());
                        futuresOrder.finishOrder();
                    }

                    divergence.ifPresent(d -> {
                        if (d.getFormerSignal().equals(Signal.GREEN) &&
                                futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {

                            String halfPositionSize =
                                    bybitPositionService
                                            .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();

                            bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), halfPositionSize);

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        String halfPositionSize =
                                bybitPositionService
                                        .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();
                        bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), halfPositionSize);
                        futuresOrder.partialDisposeOrder();
                    }

                }
            }
            // 노란색 1-4: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 하단선 도달, rsi 30 이하
            if (futuresOrder.getOrderNumber().equals(4)) {
                if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                        bollingerBand.getMiddleBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                        bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                        rsi.getValue().isLessThanOrEqual(DecimalNum.valueOf(30))) {

                    if (!positionInfo.sizeIsBiggerThan(minimumQty)) {
                        bybitTradeService
                                .takeProfit(Side.BUY, markPrice.toString(), positionInfo.getSize().toString());
                        futuresOrder.finishOrder();
                    }

                    divergence.ifPresent(d -> {
                        if (d.getFormerSignal().equals(Signal.RED) &&
                                futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                            String halfPositionSize =
                                    bybitPositionService
                                            .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();

                            bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), halfPositionSize);

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        String halfPositionSize =
                                bybitPositionService
                                        .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();
                        bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), halfPositionSize);
                        futuresOrder.partialDisposeOrder();
                    }

                }
            }
        }

        if (Signal.RED.equals(futuresOrder.getOrderSignal())) {
            // 적색 1-1: 전전봉 마감이 전봉 마감보다 낮을 때 익절, rsi가 30 이하, 포지션의 수익률이 36%(레버리지 3배 기준), 볼린저 밴드 하단에 닿을 때
            if (futuresOrder.getOrderNumber().equals(1)) {

                if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(36)) >= 0 ||
                        klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) < 0 ||
                        rsi.getValue().isLessThan(DecimalNum.valueOf(30)) ||
                        bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice))) {

                    if (!positionInfo.sizeIsBiggerThan(minimumQty)) {
                        bybitTradeService
                                .takeProfit(Side.BUY, markPrice.toString(), positionInfo.getSize().toString());
                        futuresOrder.finishOrder();
                    }

                    divergence.ifPresent(d -> {
                        if (d.getFormerSignal().equals(Signal.RED) &&
                                futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                            String halfPositionSize =
                                    bybitPositionService
                                            .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();

                            bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), halfPositionSize);

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        String halfPositionSize =
                                bybitPositionService
                                        .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();
                        bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), halfPositionSize);
                        futuresOrder.partialDisposeOrder();
                    }

                }

            }
            // 적색 1-2: 전봉 마감이 지금 마감보다 낮을 때 익절,
            // rsi가 30 이하,
            // 포지션의 수익률이 45%(레버리지 3배 기준),
            // 볼린저 밴드 하단에 닿을 때,
            if (futuresOrder.getOrderNumber().equals(2)) {
                if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(45)) >= 0 ||
                        klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) < 0 ||
                        rsi.getValue().isLessThan(DecimalNum.valueOf(30)) ||
                        bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice))) {

                    if (!positionInfo.sizeIsBiggerThan(minimumQty)) {
                        bybitTradeService
                                .takeProfit(Side.BUY, markPrice.toString(), positionInfo.getSize().toString());
                        futuresOrder.finishOrder();
                    }

                    divergence.ifPresent(d -> {
                        if (d.getFormerSignal().equals(Signal.RED) &&
                                futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                            String halfPositionSize =
                                    bybitPositionService
                                            .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();

                            bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), halfPositionSize);

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        String halfPositionSize =
                                bybitPositionService
                                        .getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString();
                        bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), halfPositionSize);
                        futuresOrder.partialDisposeOrder();
                    }

                }

            }

        }

        divergence.ifPresent(divergenceRepository::delete);
    }

    /**
     * Position이 청산됐는지에 대한 상태 확인 메서드
     * futuresOrder에 주문 상태가 활성화 돼 있으며(SIGNED, PARTIAL_DISPOSAL)
     * Bybit의 PositionInfo에 존재하지 않을 시 true 반환
     *
     * @param positionInfo Bybit에서 가져온 Position 정보
     * @param futuresOrder DB에 저장된 futures order 값
     */
    private boolean isPositionLiquidated(PositionVo positionInfo, FuturesOrder futuresOrder) {
        boolean result = false;

        if (futuresOrder.isPositionActive() && !positionInfo.isExists()) result = true;

        return result;
    }


    @Override
    @Transactional
    public void order(Signal signal) {

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

        log.info("divergenceClosePrice:{}, markPrice:{}, divergence rsi max:{}, rsi:{}",
                divergenceClosePrice.getMax(), markPrice, divergenceRsi.getMax(), rsi.getValue());

        if (Signal.GREEN.equals(signal)) {

            /**
             * 1번 째 조건
             */
            if (sma.getValue().isLessThan(DecimalNum.valueOf(klines.get(1).getClosePrice())) &&
                    sma.getValue().isLessThan(DecimalNum.valueOf(markPrice))) {

                BigDecimal price = BigDecimal.valueOf(sma.getValue().doubleValue());
                String stopLoss = price.multiply(BigDecimal.valueOf(0.95)).toString();
                newOrder(Side.BUY, price.toString(), stopLoss, signal, 1);

            }

            /**
             * 2번 째 조건
             */
            BigDecimal compareValue =
                    BigDecimal.valueOf(sma.getValue().multipliedBy(DecimalNum.valueOf(0.95)).doubleValue());
            BigDecimal middleBand = BigDecimal.valueOf(bollingerBand.getMiddleBand().doubleValue());

            if (markPrice.compareTo(compareValue) < 0 && markPrice.compareTo(middleBand) > 0) {
                BigDecimal price = BigDecimal.valueOf(bollingerBand.getMiddleBand().doubleValue());
                String stopLoss = price.multiply(BigDecimal.valueOf(0.95)).toString();
                newOrder(Side.BUY, price.toString(), stopLoss, signal, 2);
            }

        }

        if (Signal.YELLOW.equals(signal)) {

            if (markPrice.compareTo(BigDecimal.valueOf(bollingerBand.getLowerBand().doubleValue())) <= 0 &&
                    rsi.getValue().isLessThanOrEqual(DecimalNum.valueOf(30))) {
                BigDecimal previousClosePrice = klines.get(1).getClosePrice();
                String stopLoss = previousClosePrice.multiply(BigDecimal.valueOf(0.95)).toString();
                newOrder(Side.BUY, previousClosePrice.toString(), stopLoss, signal, 1);
            }

            if (markPrice.compareTo(BigDecimal.valueOf(bollingerBand.getUpperBand().doubleValue())) >= 0 &&
                    rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(70))) {
                BigDecimal previousClosePrice = klines.get(1).getClosePrice();
                String stopLoss = previousClosePrice.multiply(BigDecimal.valueOf(1.05)).toString();
                newOrder(Side.SELL, previousClosePrice.toString(), stopLoss, signal, 2);
            }

            //RSI Divergenced 시
            if (divergenceClosePrice.getMin().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) &&
                    divergenceRsi.getMin().isLessThanOrEqual(rsi.getValue())) {
                BigDecimal price = marketDataService.getMarkPrice();
                String stopLoss = price.multiply(BigDecimal.valueOf(0.95)).toString();
                newOrder(Side.BUY, price.toString(), stopLoss, signal, 3);
            }


            //RSI Divergenced 시
            if (divergenceClosePrice.getMax().isLessThanOrEqual(DecimalNum.valueOf(markPrice))
                    && divergenceRsi.getMax().isGreaterThanOrEqual(rsi.getValue())) {
                BigDecimal price = marketDataService.getMarkPrice();
                String stopLoss = price.multiply(BigDecimal.valueOf(1.05)).toString();
                newOrder(Side.SELL, price.toString(), stopLoss, signal, 4);
            }

        }

        if (Signal.RED.equals(signal)) {
            BigDecimal previousClosePrice = klines.get(1).getClosePrice();

            /**
             * 1번 째 조건
             */
            if (sma.getValue().isGreaterThan(DecimalNum.valueOf(previousClosePrice)) &&
                    sma.getValue().isGreaterThan(DecimalNum.valueOf(markPrice))) {
                BigDecimal price = BigDecimal.valueOf(sma.getValue().doubleValue());
                String stopLoss = price.multiply(BigDecimal.valueOf(1.05)).toString();
                newOrder(Side.SELL, price.toString(), stopLoss, signal, 1);
            }

            /**
             * 2번 째 조건
             */
            BigDecimal compareValue =
                    BigDecimal.valueOf(sma.getValue().multipliedBy(DecimalNum.valueOf(1.05)).doubleValue());

            BigDecimal middleBand = BigDecimal.valueOf(bollingerBand.getMiddleBand().doubleValue());

            if (markPrice.compareTo(compareValue) > 0 && markPrice.compareTo(middleBand) < 0) {
                BigDecimal price = BigDecimal.valueOf(bollingerBand.getMiddleBand().doubleValue());
                String stopLoss = price.multiply(BigDecimal.valueOf(1.05)).toString();
                newOrder(Side.SELL, price.toString(), stopLoss, signal, 2);
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