package com.example.budget.domain.trade.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.donchian.DonchianChannelLowerIndicator;
import org.ta4j.core.indicators.donchian.DonchianChannelMiddleIndicator;
import org.ta4j.core.indicators.donchian.DonchianChannelUpperIndicator;
import org.ta4j.core.num.Num;

@Getter
@AllArgsConstructor
public class DonchianChannel {

    private Num lower;
    private Num middle;
    private Num upper;

    public static DonchianChannel newInstance(BaseBarSeries series) {
        DonchianChannelLowerIndicator lower = new DonchianChannelLowerIndicator(series, 34);
        DonchianChannelMiddleIndicator middle = new DonchianChannelMiddleIndicator(series, 34);
        DonchianChannelUpperIndicator higher = new DonchianChannelUpperIndicator(series, 34);

        return new DonchianChannel(lower.getValue(series.getEndIndex()),
                middle.getValue(series.getEndIndex()),
                higher.getValue(series.getEndIndex()));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder.append("Donchian{")
                .append("lower=")
                .append(lower)
                .append(", middle=")
                .append(middle)
                .append(", upper=")
                .append(upper)
                .append('}').toString();
    }

}
