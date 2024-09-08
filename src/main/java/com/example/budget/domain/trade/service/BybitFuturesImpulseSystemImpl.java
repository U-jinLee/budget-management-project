package com.example.budget.domain.trade.service;

import com.example.budget.domain.trade.model.Signal;
import org.springframework.stereotype.Component;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

@Component
public class BybitFuturesImpulseSystemImpl implements ImpulseSystem {

    @Override
    public Signal getSignal(Num maSlope, Num macdSlope) {
        if (isSignalGreen(maSlope, macdSlope))
            return Signal.GREEN;

        if (isSignalYellow(maSlope, macdSlope))
            return Signal.YELLOW;

        if (isSignalRed(maSlope, macdSlope))
            return Signal.RED;

        return Signal.UNKNOWN;
    }

    private boolean isSignalGreen(Num maSlope, Num macdSlope) {
        return maSlope.isGreaterThan(DecimalNum.valueOf(0)) && macdSlope.isGreaterThan(DecimalNum.valueOf(0));
    }

    private boolean isSignalYellow(Num maSlope, Num macdSlope) {
        return (maSlope.isGreaterThan(DecimalNum.valueOf(0)) && macdSlope.isLessThan(DecimalNum.valueOf(0))) ||
                (maSlope.isLessThan(DecimalNum.valueOf(0)) && macdSlope.isGreaterThan(DecimalNum.valueOf(0)));
    }

    private boolean isSignalRed(Num maSlope, Num macdSlope) {
        return maSlope.isLessThan(DecimalNum.valueOf(0)) && macdSlope.isLessThan(DecimalNum.valueOf(0));
    }

}