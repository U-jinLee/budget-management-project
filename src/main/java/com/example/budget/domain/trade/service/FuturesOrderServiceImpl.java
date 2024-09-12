package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.account.AccountType;
import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.domain.market.MarketInterval;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.position.TpslMode;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.restApi.BybitApiAccountRestClient;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import com.example.budget.domain.trade.dto.*;
import com.example.budget.domain.trade.exception.OrderNotFoundException;
import com.example.budget.domain.trade.exception.SignalUnknownException;
import com.example.budget.domain.trade.model.*;
import com.example.budget.domain.trade.repository.DivergenceRepository;
import com.example.budget.domain.trade.repository.FuturesOrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.num.DecimalNum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


    @Override
    @Transactional
    public void partialDisposalTakeProfit() {
        futuresOrderRepository.findByOrderStatus(OrderStatus.PARTIAL_DISPOSAL).ifPresent(o -> {
            if (isPositionExsits()) {
                Boolean isTakeProfitDone = false;
                PositionVo positionInfo = getPositionInfo();
                BigDecimal markPrice = getMarkPrice();

                List<KlineDto> klines =
                        this.getFuturesHistoricalKlines(MarketInterval.TWELVE_HOURLY, 200, true);
                BarSeriesUtil barSeries = new BarSeriesUtil(klines);

                BollingerBandDto bollingerBand = barSeries.bollingerBand(50, 2.1);
                RsiDto rsi = barSeries.rsi();

                Collections.reverse(klines);

                //녹색:
                if (Signal.GREEN.equals(o.getSignal())) {
                    // 녹색 1-1: 전전봉 마감이 전봉 마감보다 높을 때 익절,
                    // rsi가 75 이상,
                    // 포지션의 수익률이 36%(레버리지 3배 기준),
                    // 볼린저 밴드 상단에 닿을 때
                    if (o.getOrderNumber().equals(1)) {

                        if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(36)) >= 0 ||
                                klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) > 0 ||
                                rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(75)) ||
                                bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice))) {
                            newOrder(Side.SELL, markPrice.toString(), positionInfo.getSize().toString());
                            isTakeProfitDone = true;
                        }

                    }

                    // 녹색 1-2: 전전봉 마감이 전봉 마감보다 높을 때 익절,
                    // rsi가 75 이상,
                    // 포지션의 수익률이 45%(레버리지 3배 기준),
                    // 볼린저 밴드 상단에 닿을 때,
                    if (o.getOrderNumber().equals(2)) {
                        if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(45)) >= 0 ||
                                klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) > 0 ||
                                rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(75)) ||
                                bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice))) {
                            newOrder(Side.SELL, markPrice.toString(), positionInfo.getSize().toString());
                            isTakeProfitDone = true;
                        }

                    }

                }

                //노란색:
                if (Signal.YELLOW.equals(o.getSignal())) {
                    // 노란색 1-1: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 상단선 도달, rsi 70 이상
                    if (o.getOrderNumber().equals(1)) {
                        if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                                bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                                bollingerBand.getMiddleBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                                rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(70))) {
                            newOrder(Side.SELL, markPrice.toString(), positionInfo.getSize().toString());
                            isTakeProfitDone = true;
                        }

                    }
                    // 노란색 1-2: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 하단선 도달, rsi 30 이하
                    if (o.getOrderNumber().equals(2)) {
                        if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                                bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                                bollingerBand.getMiddleBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                                rsi.getValue().isLessThanOrEqual(DecimalNum.valueOf(30))) {
                            newOrder(Side.BUY, markPrice.toString(), positionInfo.getSize().toString());
                            isTakeProfitDone = true;
                        }
                    }
                    // 노란색 1-3: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 상단선 도달, rsi 70 이상
                    if (o.getOrderNumber().equals(3)) {
                        if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                                bollingerBand.getMiddleBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                                bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                                rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(70))
                        ) {
                            newOrder(Side.SELL, markPrice.toString(), positionInfo.getSize().toString());
                            isTakeProfitDone = true;
                        }
                    }
                    // 노란색 1-4: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 하단선 도달, rsi 30 이하
                    if (o.getOrderNumber().equals(4)) {
                        if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                                bollingerBand.getMiddleBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                                bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                                rsi.getValue().isLessThanOrEqual(DecimalNum.valueOf(30))) {
                            newOrder(Side.BUY, markPrice.toString(), positionInfo.getSize().toString());
                            isTakeProfitDone = true;
                        }
                    }
                }

                if (Signal.RED.equals(o.getSignal())) {
                    // 적색 1-1: 전전봉 마감이 전봉 마감보다 낮을 때 익절, rsi가 30 이하, 포지션의 수익률이 36%(레버리지 3배 기준), 볼린저 밴드 하단에 닿을 때
                    if (o.getOrderNumber().equals(1)) {

                        if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(36)) >= 0 ||
                                klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) < 0 ||
                                rsi.getValue().isLessThan(DecimalNum.valueOf(30)) ||
                                bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice))) {
                            newOrder(Side.BUY, markPrice.toString(), positionInfo.getSize().toString());
                            isTakeProfitDone = true;
                        }

                    }
                    // 적색 1-2: 전봉 마감이 지금 마감보다 낮을 때 익절,
                    // rsi가 30 이하,
                    // 포지션의 수익률이 45%(레버리지 3배 기준),
                    // 볼린저 밴드 하단에 닿을 때,
                    if (o.getOrderNumber().equals(2)) {
                        if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(45)) >= 0 ||
                                klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) < 0 ||
                                rsi.getValue().isLessThan(DecimalNum.valueOf(30)) ||
                                bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice))) {
                            newOrder(Side.BUY, markPrice.toString(), positionInfo.getSize().toString());
                            isTakeProfitDone = true;
                        }

                    }

                }

                if (isTakeProfitDone.equals(true)) {
                    o.finishOrder();
                } else {
                    o.reSigned();
                }

            } else {
                o.cancelOrder();
            }
        });
    }

    /**
     * 12시간 봉 기준
     */
    @Override
    @Transactional
    public void takeProfit() {
        if (futuresOrderRepository.findByOrderStatus(OrderStatus.PARTIAL_DISPOSAL).isPresent()) {
            log.info("Partial disposal exist");
        } else {
            List<KlineDto> klines = this.getFuturesHistoricalKlines(MarketInterval.TWELVE_HOURLY, 200, true);
            BarSeriesUtil barSeries = new BarSeriesUtil(klines);
            BollingerBandDto bollingerBand = barSeries.bollingerBand(50, 2.1);
            RsiDto rsi = barSeries.rsi();

            Collections.reverse(klines);

            FuturesOrder futuresOrder = futuresOrderRepository.findByOrderStatus(OrderStatus.SIGNED)
                    .orElseThrow(OrderNotFoundException::new);

            BigDecimal markPrice = getMarkPrice();
            PositionVo positionInfo = getPositionInfo();

            //If divergence occurs then dispose The position at the market price
            divergenceRepository.findByDivergenceType(DivergenceType.TAKE_PROFIT).ifPresent(d -> {

                newMarketOrder(positionInfo.getSide().equals("Sell") ? Side.BUY : Side.SELL,
                        positionInfo.getSize().toString());

                futuresOrder.cancelOrder();

                divergenceRepository.delete(d);
            });

            //녹색:
            if (Signal.GREEN.equals(futuresOrder.getSignal())) {
                // 녹색 1-1: 전전봉 마감이 전봉 마감보다 높을 때 익절,
                // rsi가 75 이상,
                // 포지션의 수익률이 36%(레버리지 3배 기준),
                // 볼린저 밴드 상단에 닿을 때
                if (futuresOrder.getOrderNumber().equals(1)) {
                    if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(36)) >= 0 ||
                            klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) > 0 ||
                            rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(75)) ||
                            bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice))) {

                        BigDecimal minimumQty =
                                calculateOrderQuantity(BigDecimal.valueOf(0.04), getAvailableBalance());

                        if (positionInfo.getSize().compareTo(minimumQty) <= 0) {
                            newOrder(Side.SELL, markPrice.toString(), positionInfo.getSide());
                            futuresOrder.finishOrder();
                        } else {
                            disposalOrder(Side.SELL, futuresOrder, markPrice);
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

                        BigDecimal minimumQty =
                                calculateOrderQuantity(BigDecimal.valueOf(0.04), getAvailableBalance());

                        if (positionInfo.getSize().compareTo(minimumQty) <= 0) {
                            newOrder(Side.SELL, markPrice.toString(), positionInfo.getSide());
                            futuresOrder.finishOrder();
                        } else {
                            disposalOrder(Side.SELL, futuresOrder, markPrice);
                        }

                    }

                }

            }

            //노란색:
            if (Signal.YELLOW.equals(futuresOrder.getSignal())) {
                // 노란색 1-1: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 상단선 도달, rsi 70 이상
                if (futuresOrder.getOrderNumber().equals(1)) {
                    if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                            bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                            bollingerBand.getMiddleBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                            rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(70))) {

                        BigDecimal minimumQty =
                                calculateOrderQuantity(BigDecimal.valueOf(0.04), getAvailableBalance());

                        if (positionInfo.getSize().compareTo(minimumQty) <= 0) {
                            newOrder(Side.SELL, markPrice.toString(), positionInfo.getSide());
                            futuresOrder.finishOrder();
                        } else {
                            disposalOrder(Side.SELL, futuresOrder, markPrice);
                        }

                    }

                }
                // 노란색 1-2: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 하단선 도달, rsi 30 이하
                if (futuresOrder.getOrderNumber().equals(2)) {
                    if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                            bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                            bollingerBand.getMiddleBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                            rsi.getValue().isLessThanOrEqual(DecimalNum.valueOf(30))) {

                        BigDecimal minimumQty =
                                calculateOrderQuantity(BigDecimal.valueOf(0.04), getAvailableBalance());

                        if (positionInfo.getSize().compareTo(minimumQty) <= 0) {
                            newOrder(Side.BUY, markPrice.toString(), positionInfo.getSide());
                            futuresOrder.finishOrder();
                        } else {
                            disposalOrder(Side.BUY, futuresOrder, markPrice);
                        }

                    }
                }
                // 노란색 1-3: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 상단선 도달, rsi 70 이상
                if (futuresOrder.getOrderNumber().equals(3)) {
                    if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                            bollingerBand.getMiddleBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                            bollingerBand.getUpperBand().isLessThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                            rsi.getValue().isGreaterThanOrEqual(DecimalNum.valueOf(70))
                    ) {

                        BigDecimal minimumQty =
                                calculateOrderQuantity(BigDecimal.valueOf(0.04), getAvailableBalance());

                        if (positionInfo.getSize().compareTo(minimumQty) <= 0) {
                            newOrder(Side.SELL, markPrice.toString(), positionInfo.getSide());
                            futuresOrder.finishOrder();
                        } else {
                            disposalOrder(Side.SELL, futuresOrder, markPrice);
                        }

                    }
                }
                // 노란색 1-4: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 하단선 도달, rsi 30 이하
                if (futuresOrder.getOrderNumber().equals(4)) {
                    if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
                            bollingerBand.getMiddleBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                            bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice)) ||
                            rsi.getValue().isLessThanOrEqual(DecimalNum.valueOf(30))) {

                        BigDecimal minimumQty =
                                calculateOrderQuantity(BigDecimal.valueOf(0.04), getAvailableBalance());

                        if (positionInfo.getSize().compareTo(minimumQty) <= 0) {
                            newOrder(Side.BUY, markPrice.toString(), positionInfo.getSide());
                            futuresOrder.finishOrder();
                        } else {
                            disposalOrder(Side.BUY, futuresOrder, markPrice);
                        }

                    }
                }
            }

            if (Signal.RED.equals(futuresOrder.getSignal())) {
                // 적색 1-1: 전전봉 마감이 전봉 마감보다 낮을 때 익절, rsi가 30 이하, 포지션의 수익률이 36%(레버리지 3배 기준), 볼린저 밴드 하단에 닿을 때
                if (futuresOrder.getOrderNumber().equals(1)) {

                    if (positionInfo.getRoi().compareTo(BigDecimal.valueOf(36)) >= 0 ||
                            klines.get(2).getClosePrice().compareTo(klines.get(1).getClosePrice()) < 0 ||
                            rsi.getValue().isLessThan(DecimalNum.valueOf(30)) ||
                            bollingerBand.getLowerBand().isGreaterThanOrEqual(DecimalNum.valueOf(markPrice))) {

                        BigDecimal minimumQty =
                                calculateOrderQuantity(BigDecimal.valueOf(0.04), getAvailableBalance());

                        if (positionInfo.getSize().compareTo(minimumQty) <= 0) {
                            newOrder(Side.BUY, markPrice.toString(), positionInfo.getSide());
                            futuresOrder.finishOrder();
                        } else {
                            disposalOrder(Side.BUY, futuresOrder, markPrice);
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

                        BigDecimal minimumQty =
                                calculateOrderQuantity(BigDecimal.valueOf(0.04), getAvailableBalance());

                        if (positionInfo.getSize().compareTo(minimumQty) <= 0) {
                            newOrder(Side.BUY, markPrice.toString(), positionInfo.getSide());
                            futuresOrder.finishOrder();
                        } else {
                            disposalOrder(Side.BUY, futuresOrder, markPrice);
                        }

                    }

                }

            }

        }
    }

    private void disposalOrder(Side side, FuturesOrder futuresOrder, BigDecimal markPrice) {
        if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
            newOrder(side, markPrice.toString(),
                    getPositionInfo().getSize().divide(BigDecimal.valueOf(2)).toString());
            futuresOrder.partialDisposeOrder();
        }
    }

    @Override
    @Transactional
    public void order(Signal signal) {

        if (Signal.UNKNOWN.equals(signal)) throw new SignalUnknownException();


        /**
         * If there is a position with a reservation at stake, delete the existing position and enter a new position
         */
        if (isOutstandingOrderExist()) {
            cancelAllOrder();
        } else {
            futuresOrderRepository.findByOrderStatus(OrderStatus.SIGNED).ifPresent(o -> {
                o.cancelOrder();
                futuresOrderRepository.saveAndFlush(o);
            });
        }

        List<KlineDto> klines = this.getFuturesHistoricalKlines(MarketInterval.TWELVE_HOURLY, 200, true);
        BarSeriesUtil barSeries = new BarSeriesUtil(klines);
        BollingerBandDto bollingerBand = barSeries.bollingerBand(50, 2.1);
        RsiDto rsi = barSeries.rsi();
        MaDto sma = barSeries.sma(16);

        RsiDto divergenceRsi = barSeries.rsi(175, 199);
        MinAndMaxDto divergenceClosePrice = barSeries.closePrice(175, 199);

        Collections.reverse(klines);

        BigDecimal markPrice = getMarkPrice();
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
                BigDecimal price = getMarkPrice();
                String stopLoss = price.multiply(BigDecimal.valueOf(0.95)).toString();
                newOrder(Side.BUY, price.toString(), stopLoss, signal, 3);
            }


            //RSI Divergenced 시
            if (divergenceClosePrice.getMax().isLessThanOrEqual(DecimalNum.valueOf(markPrice))
                    && divergenceRsi.getMax().isGreaterThanOrEqual(rsi.getValue())) {
                BigDecimal price = getMarkPrice();
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

    private void newMarketOrder(Side side, String quantity) {
        BybitApiTradeRestClient tradeClient = bybitApiClientFactory.newTradeRestClient();

        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .side(side)
                .orderType(TradeOrderType.MARKET)
                .qty(quantity)
                .build();

        tradeClient.createOrder(request);
    }

    private void newOrder(Side side, String price, String quantity) {
        BybitApiTradeRestClient tradeClient = bybitApiClientFactory.newTradeRestClient();

        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .side(side)
                .orderType(TradeOrderType.LIMIT)
                .price(price)
                .qty(quantity)
                .build();

        tradeClient.createOrder(request);
    }

    private void newOrder(Side side, String price, String stopLoss, Signal signal, int orderNumber) {
        BybitApiTradeRestClient tradeClient = bybitApiClientFactory.newTradeRestClient();

        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .side(side)
                .orderType(TradeOrderType.LIMIT)
                .price(price)
                .tpslMode(TpslMode.PARTIAL.getDescription())
                .stopLoss(stopLoss)
                .qty(calculateOrderQuantity(getAvailableBalance()))
                .build();

        futuresOrderRepository.save(
                FuturesOrder.builder()
                        .signal(signal)
                        .orderNumber(orderNumber)
                        .orderStatus(OrderStatus.SIGNED)
                        .build());

        tradeClient.createOrder(request);
    }

    /**
     * Calculate coin's order quantity
     *
     * @param balance My balance information
     * @return Purchase quantity
     */
    private String calculateOrderQuantity(BigDecimal balance) {
        return balance.multiply(POSITION_SIZE).divide(getMarkPrice(), 3, RoundingMode.HALF_UP).toString();
    }

    /**
     * Calculate coin's order quantity
     *
     * @param positionSize Position size
     * @param balance      My balance information
     * @return Purchase quantity
     */
    private BigDecimal calculateOrderQuantity(BigDecimal positionSize, BigDecimal balance) {
        return balance.multiply(positionSize).divide(getMarkPrice(), 3, RoundingMode.HALF_UP);
    }

    /**
     * Get my order available balance
     *
     * @return My order available balance
     */
    private BigDecimal getAvailableBalance() {
        BybitApiAccountRestClient client = bybitApiClientFactory.newAccountRestClient();

        AccountDataRequest request = AccountDataRequest.builder()
                .accountType(AccountType.UNIFIED)
                .build();

        BigDecimal result = BigDecimal.ZERO;

        try {
            String jsonString = new ObjectMapper().writeValueAsString(client.getWalletBalance(request));

            result = new Gson().fromJson(jsonString, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonArray("coin")
                    .get(3)
                    .getAsJsonObject()
                    .get("availableToWithdraw")
                    .getAsBigDecimal();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private BigDecimal getMarkPrice() {
        MarketDataRequest request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .build();

        var marketTickers = bybitApiClientFactory.newMarketDataRestClient().getMarketTickers(request);

        BigDecimal markPrice = null;
        try {
            String result = new ObjectMapper().writeValueAsString(marketTickers);
            JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);

            markPrice = jsonObject.getAsJsonObject("result")
                    .getAsJsonArray("list")
                    .get(0).getAsJsonObject().get("markPrice").getAsBigDecimal();

        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }

        return markPrice;
    }

    @Override
    public List<KlineDto> getFuturesHistoricalKlines(MarketInterval interval, int limit, boolean isReverse) {

        BybitApiMarketRestClient client = bybitApiClientFactory.newMarketDataRestClient();

        MarketDataRequest requestParameter = MarketDataRequest.builder().category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .limit(limit)
                .marketInterval(interval).build();

        String requestResult = client.getMarketLinesData(requestParameter).toString();

        JsonArray jsonArray = new Gson().fromJson(requestResult, JsonObject.class)
                .getAsJsonObject("result")
                .getAsJsonArray("list");

        List<KlineDto> result = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonArray data = jsonElement.getAsJsonArray();
            result.add(KlineDto.newInstance(data));
        }

        if (isReverse) Collections.reverse(result);

        return result;
    }

    @Override
    public boolean isOutstandingOrderExist() {
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

    public void cancelAllOrder() {
        BybitApiTradeRestClient tradeClient = bybitApiClientFactory.newTradeRestClient();

        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .build();

        FuturesOrder futuresOrder = futuresOrderRepository.findByOrderStatus(OrderStatus.SIGNED)
                .orElseThrow(OrderNotFoundException::new);

        tradeClient.cancelAllOrder(request);

        futuresOrder.cancelOrder();
    }

    @Override
    public boolean isPositionExsits() {
        BybitApiPositionRestClient client = bybitApiClientFactory.newPositionRestClient();

        PositionDataRequest request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .build();

        BigDecimal result = null;
        try {
            String json = new ObjectMapper().writeValueAsString(client.getPositionInfo(request));

            result = new Gson().fromJson(json, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list")
                    .get(0)
                    .getAsJsonObject()
                    .get("size")
                    .getAsBigDecimal();
        } catch (Exception e) {
            e.printStackTrace();
        }

        result = result == null ? BigDecimal.ZERO : result;

        return !result.equals(BigDecimal.ZERO);
    }

    public PositionVo getPositionInfo() {
        BybitApiPositionRestClient client = bybitApiClientFactory.newPositionRestClient();

        PositionDataRequest request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .build();

        PositionVo result = PositionVo.newInstance();

        try {
            String json = new ObjectMapper().writeValueAsString(client.getPositionInfo(request));

            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list")
                    .get(0)
                    .getAsJsonObject();

            result = new PositionVo(
                    jsonObject.get("symbol").getAsString(),
                    jsonObject.get("side").getAsString(),
                    jsonObject.get("positionBalance").getAsBigDecimal(),
                    jsonObject.get("unrealisedPnl").getAsBigDecimal(),
                    jsonObject.get("size").getAsBigDecimal());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}