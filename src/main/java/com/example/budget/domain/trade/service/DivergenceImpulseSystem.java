package com.example.budget.domain.trade.service;

import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.dto.RsiDto;
import com.example.budget.domain.trade.model.Divergence;
import com.example.budget.domain.trade.model.DivergenceType;
import com.example.budget.domain.trade.model.Signal;
import com.example.budget.domain.trade.repository.DivergenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ta4j.core.num.DecimalNum;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DivergenceImpulseSystem {

    private final DivergenceRepository divergenceRepository;

    public Signal checkDivergence
            (Signal signal, BarSeriesUtil barSeries, List<KlineDto> klines, DivergenceType divergenceType) {

        Signal formerSignal = Signal.valueOf(signal.name());

        if (isSignalYellow(signal)) return signal;

        signal = Signal.GREEN.equals(signal) ?
                checkGreenSignalDivergence(signal, barSeries, klines) :
                checkRedSignalDivergence(signal, barSeries, klines);

        if (!formerSignal.equals(signal)) saveDivergence(formerSignal, signal, divergenceType);

        return signal;
    }

    private boolean isSignalYellow(Signal signal) {
        return Signal.YELLOW.equals(signal);
    }

    private Signal checkGreenSignalDivergence(Signal signal, BarSeriesUtil barSeries, List<KlineDto> klines) {
        // Donchian 모델의 가장 높은 가격이 전 종가의 높은 가격보다 작거나 같을 때
        DecimalNum highPrice = DecimalNum.valueOf(klines.get(klines.size() - 2).getHighPrice());

        if (barSeries.donchianChannel().getUpper().isLessThanOrEqual(highPrice)) {
            RsiDto rsi = barSeries.rsi();
            //todo:
            if (rsi.getMax().isGreaterThan(rsi.getValue())) {
                signal = Signal.YELLOW;
                if (rsi.getValue().isGreaterThan(DecimalNum.valueOf(70.0))) signal = Signal.GREEN;
            }
        }

        if (barSeries.rsi().getValue().isGreaterThan(DecimalNum.valueOf(80.0))) signal = Signal.YELLOW;

        return signal;
    }

    private Signal checkRedSignalDivergence(Signal signal, BarSeriesUtil barSeries, List<KlineDto> klines) {
        // Donchian 모델의 가장 높은 가격이 전 종가의 낮은 가격보다 크거나 같을 때
        DecimalNum lowPrice = DecimalNum.valueOf(klines.get(klines.size() - 2).getLowPrice());

        if (barSeries.donchianChannel().getLower().isGreaterThanOrEqual(lowPrice)) {
            RsiDto rsi = barSeries.rsi();
            //todo:
            if (rsi.getMin().isLessThan(rsi.getValue())) {
                signal = Signal.YELLOW;
                if (rsi.getValue().isLessThan(DecimalNum.valueOf(30.0))) signal = Signal.RED;
            }
        }

        if (barSeries.rsi().getValue().isLessThan(DecimalNum.valueOf(25.0))) signal = Signal.YELLOW;

        return signal;
    }

    private void saveDivergence(Signal formerSignal, Signal changeSignal, DivergenceType divergenceType) {

        Divergence divergence = Divergence.builder()
                .formerSignal(formerSignal)
                .changeSignal(changeSignal)
                .divergenceType(divergenceType)
                .build();

        Divergence result = divergenceRepository.save(divergence);

        log.info("Divergence Occurred::{}", result);
    }

}