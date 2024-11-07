package com.example.budget.domain.trade.service;

import com.example.budget.domain.trade.dto.*;
import com.example.budget.domain.trade.model.DonchianChannel;
import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.donchian.DonchianChannelLowerIndicator;
import org.ta4j.core.indicators.donchian.DonchianChannelMiddleIndicator;
import org.ta4j.core.indicators.donchian.DonchianChannelUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class BarSeriesUtil {

    private List<KlineDto> klines;
    private BaseBarSeries series;
    private ClosePriceIndicator closePrice;

    public BarSeriesUtil(List<KlineDto> klines) {
        this.klines = klines;
        this.series = new BaseBarSeries();

        for (int i = 0; i < this.klines.size(); i++) {
            ZonedDateTime zonedDateTime =
                    Instant.ofEpochMilli(this.klines.get(i).getCloseTime()).atZone(ZoneId.of("UTC"));

            this.series.addBar(
                    zonedDateTime,
                    klines.get(i).getOpenPrice(),
                    klines.get(i).getHighPrice(),
                    klines.get(i).getLowPrice(),
                    klines.get(i).getClosePrice(),
                    klines.get(i).getVolume()
            );
        }

        this.closePrice = new ClosePriceIndicator(this.series);
    }

    public MaDto ema(int barCount) {
        EMAIndicator ma = new EMAIndicator(this.closePrice, barCount);

        int currentIndex = this.series.getEndIndex();

        Num currentMa = null;
        Num slope = null;
        if(currentIndex > 0) {
            currentMa = ma.getValue(currentIndex);
            Num previousMa = ma.getValue(currentIndex - 1);
            slope = currentMa.minus(previousMa);
        }

        return new MaDto(currentMa, slope);
    }

    public MaDto sma(int barCount) {
        SMAIndicator ma = new SMAIndicator(this.closePrice, barCount);

        int currentIndex = this.series.getEndIndex();
        Num currentMa = null;
        Num slope = null;
        if(currentIndex > 0) {
            currentMa = ma.getValue(currentIndex);
            Num previousMa = ma.getValue(currentIndex - 1);
            slope = currentMa.minus(previousMa);
        }

        return new MaDto(currentMa, slope);
    }

    public MacdDto macd() {
        int endIndex = this.series.getEndIndex();
        int startIndex = Math.max(endIndex - 33, 0);

        MACDIndicator macd = new MACDIndicator(this.closePrice, 12, 26);
        EMAIndicator signalLine = new EMAIndicator(macd, 9);

        List<Num> latestMacdValues = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            latestMacdValues.add(macd.getValue(i).minus(signalLine.getValue(i)));
        }

        Num maxMacdValue = latestMacdValues.get(0);
        Num minMacdValue = latestMacdValues.get(0);

        for (Num macdValue : latestMacdValues) {
            if (macdValue.isGreaterThan(maxMacdValue)) {
                maxMacdValue = macdValue;
            }
            if (macdValue.isLessThan(minMacdValue)) {
                minMacdValue = macdValue;
            }
        }

        Num macdHistogram = macd.getValue(endIndex).minus(signalLine.getValue(endIndex));

        Num yesterdayMacdHistogram = macd.getValue(endIndex - 1).minus(signalLine.getValue(endIndex - 1));
        Num histogramSlope = macdHistogram.minus(yesterdayMacdHistogram);

        return new MacdDto(macd.getValue(endIndex),
                signalLine.getValue(endIndex), histogramSlope,
                macd.getValue(endIndex).minus(signalLine.getValue(endIndex)), maxMacdValue, minMacdValue);
    }

    public MinAndMaxDto closePrice(int startIndex, int endIndex) {
        List<KlineDto> calculateKLines = new ArrayList<>(klines);

        List<Num> latestClosePriceValues = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            latestClosePriceValues.add(DecimalNum.valueOf(calculateKLines.get(i).getClosePrice()));
        }

        Num maxClosePriceValue = latestClosePriceValues.get(0);
        Num minClosePriceRsiValue = latestClosePriceValues.get(0);

        for (Num rsiValue : latestClosePriceValues) {
            if (rsiValue.isGreaterThan(maxClosePriceValue)) {
                maxClosePriceValue = rsiValue;
            }
            if (rsiValue.isLessThan(minClosePriceRsiValue)) {
                minClosePriceRsiValue = rsiValue;
            }
        }

        return new MinAndMaxDto(minClosePriceRsiValue, maxClosePriceValue);
    }

    public RsiDto rsi(int startIndex, int endIndex) {
        RSIIndicator rsi = new RSIIndicator(this.closePrice, 14);

        List<Num> latestRsiValues = new ArrayList<>();

        for (int i = startIndex; i <= endIndex; i++) {
            latestRsiValues.add(rsi.getValue(i));
        }

        Num maxRsiValue = latestRsiValues.get(0);
        Num minRsiValue = latestRsiValues.get(0);

        for (Num rsiValue : latestRsiValues) {
            if (rsiValue.isGreaterThan(maxRsiValue)) {
                maxRsiValue = rsiValue;
            }
            if (rsiValue.isLessThan(minRsiValue)) {
                minRsiValue = rsiValue;
            }
        }

        Num rsiValue = rsi.getValue(this.series.getEndIndex());

        return new RsiDto(rsiValue, maxRsiValue, minRsiValue);
    }

    public RsiDto rsi() {
        RSIIndicator rsi = new RSIIndicator(this.closePrice, 14);

        int endIndex = this.series.getEndIndex();
        int startIndex = Math.max(endIndex - 33, 0);

        List<Num> latestRsiValues = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            latestRsiValues.add(rsi.getValue(i));
        }

        Num maxRsiValue = latestRsiValues.get(0);
        Num minRsiValue = latestRsiValues.get(0);

        for (Num rsiValue : latestRsiValues) {
            if (rsiValue.isGreaterThan(maxRsiValue)) {
                maxRsiValue = rsiValue;
            }
            if (rsiValue.isLessThan(minRsiValue)) {
                minRsiValue = rsiValue;
            }
        }

        Num rsiValue = rsi.getValue(this.series.getEndIndex());

        return new RsiDto(rsiValue, maxRsiValue, minRsiValue);
    }

    public RsiDto rsi(int index) {
        RSIIndicator rsi = new RSIIndicator(this.closePrice, 14);

        int startIndex = Math.max(index - 33, 0);

        List<Num> latestRsiValues = new ArrayList<>();
        for (int i = startIndex; i <= index; i++) {
            latestRsiValues.add(rsi.getValue(i));
        }

        Num maxRsiValue = latestRsiValues.get(0);
        Num minRsiValue = latestRsiValues.get(0);

        for (Num rsiValue : latestRsiValues) {
            if (rsiValue.isGreaterThan(maxRsiValue)) {
                maxRsiValue = rsiValue;
            }
            if (rsiValue.isLessThan(minRsiValue)) {
                minRsiValue = rsiValue;
            }
        }

        Num rsiValue = rsi.getValue(index);
        return new RsiDto(rsiValue, maxRsiValue, minRsiValue);
    }

    /**
     *
     * @param barCount
     * @param stdDev - Standard Deviation
     * @return BollingerBandDto - Return Bollinger Band's High, Middle and Low Price
     */
    public BollingerBandDto bollingerBand(int barCount, Number stdDev) {

        StandardDeviationIndicator standardDeviation = new StandardDeviationIndicator(this.closePrice, barCount);

        SMAIndicator sma20 = new SMAIndicator(this.closePrice, barCount);
        Num stdDevMultiplier = this.series.numOf(stdDev);

        BollingerBandsMiddleIndicator middleBand = new BollingerBandsMiddleIndicator(sma20);

        BollingerBandsUpperIndicator upperBand =
                new BollingerBandsUpperIndicator(middleBand, standardDeviation, stdDevMultiplier);

        BollingerBandsLowerIndicator lowerBand =
                new BollingerBandsLowerIndicator(middleBand, standardDeviation, stdDevMultiplier);

        // 결과 출력 예제 (생략)
        int endIndex = this.closePrice.getBarSeries().getEndIndex();

        return new BollingerBandDto
                (upperBand.getValue(endIndex), middleBand.getValue(endIndex), lowerBand.getValue(endIndex));

    }

    public DonchianChannel donchianChannel() {
        DonchianChannelLowerIndicator lower = new DonchianChannelLowerIndicator(this.series, 34);
        DonchianChannelMiddleIndicator middle = new DonchianChannelMiddleIndicator(this.series, 34);
        DonchianChannelUpperIndicator higher = new DonchianChannelUpperIndicator(this.series, 34);

        return new DonchianChannel(lower.getValue(this.series.getEndIndex()),
                middle.getValue(this.series.getEndIndex()),
                higher.getValue(this.series.getEndIndex()));
    }

}