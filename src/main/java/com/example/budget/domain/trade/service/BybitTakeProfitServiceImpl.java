package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.market.MarketInterval;
import com.bybit.api.client.domain.trade.Side;
import com.example.budget.domain.trade.dto.BollingerBandDto;
import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.dto.RsiDto;
import com.example.budget.domain.trade.exception.OrderNotFoundException;
import com.example.budget.domain.trade.exception.PositionIsLiquidatedException;
import com.example.budget.domain.trade.model.*;
import com.example.budget.domain.trade.repository.DivergenceRepository;
import com.example.budget.domain.trade.repository.FuturesOrderRepository;
import com.example.budget.global.util.ShortSafetyPositionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ta4j.core.num.DecimalNum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BybitTakeProfitServiceImpl implements TakeProfitService {

    private final FuturesOrderRepository futuresOrderRepository;
    private final DivergenceRepository divergenceRepository;
    private final MarketDataService marketDataService;
    private final BybitPositionService bybitPositionService;
    private final BybitAccountService bybitAccountService;
    private final BybitTradeService bybitTradeService;

    @Override
    @Transactional
    public void execute(Signal signal) {
        List<OrderStatus> orderStatuses = new ArrayList<>();
        orderStatuses.add(OrderStatus.SIGNED);
        orderStatuses.add(OrderStatus.PARTIAL_DISPOSAL);

        FuturesOrder futuresOrder = futuresOrderRepository.findByOrderStatusIn(orderStatuses)
                .orElseThrow(OrderNotFoundException::new);

        PositionVo positionInfo = bybitPositionService.getPositionInfo();
        BigDecimal positionsHalfSize = positionInfo.getHalfSize();

        BigDecimal markPrice = marketDataService.getMarkPrice();

        if (isPositionLiquidated(positionInfo, futuresOrder)) {
            futuresOrder.liquidatedPosition();
            throw new PositionIsLiquidatedException();
        }

        List<KlineDto> klines =
                marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);

        boolean isPositiveClose = ShortSafetyPositionUtil.isPositiveClose(klines);

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

                            bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), positionsHalfSize.toString());

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {

                        bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), positionsHalfSize.toString());

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

                            bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), positionsHalfSize.toString());

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), positionsHalfSize.toString());
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

                            bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), positionsHalfSize.toString());

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), positionsHalfSize.toString());
                        futuresOrder.partialDisposeOrder();
                    }

                }

            }
            // 노란색 1-2: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 하단선 도달, rsi 30 이하
            if (futuresOrder.getOrderNumber().equals(2)) {
                if (isPositiveClose ||
                        positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
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

                            bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), positionsHalfSize.toString());

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), positionsHalfSize.toString());

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

                            bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), positionsHalfSize.toString());

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        bybitTradeService.takeProfit(Side.SELL, markPrice.toString(), positionsHalfSize.toString());

                        futuresOrder.partialDisposeOrder();
                    }

                }
            }
            // 노란색 1-4: 포지션의 수익률 30%, 볼린저 밴드 중간선 도달, 볼린저 밴드 하단선 도달, rsi 30 이하
            if (futuresOrder.getOrderNumber().equals(4)) {
                if (isPositiveClose ||
                        positionInfo.getRoi().compareTo(BigDecimal.valueOf(30)) >= 0 ||
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

                            bybitTradeService
                                    .takeProfit(Side.BUY, markPrice.toString(), positionsHalfSize.toString());

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {

                        bybitTradeService
                                .takeProfit(Side.BUY, markPrice.toString(), positionsHalfSize.toString());

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

                            bybitTradeService
                                    .takeProfit(Side.BUY, markPrice.toString(), positionsHalfSize.toString());

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {
                        bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), positionsHalfSize.toString());
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

                            bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), positionsHalfSize.toString());

                            futuresOrder.partialDisposeOrder();
                        }
                    });

                    if (futuresOrder.getOrderStatus().equals(OrderStatus.SIGNED)) {

                        bybitTradeService.takeProfit(Side.BUY, markPrice.toString(), positionsHalfSize.toString());

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

}
