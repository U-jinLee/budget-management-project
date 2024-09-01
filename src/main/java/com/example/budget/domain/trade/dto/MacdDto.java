package com.example.budget.domain.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.ta4j.core.num.Num;

@Getter
@ToString
@AllArgsConstructor
public class MacdDto {
    private Num macdHistogram;
    private Num signalLine;
    private Num histogramSlope;
    private Num macd;
    private Num maxMacd;
    private Num minMacd;
}