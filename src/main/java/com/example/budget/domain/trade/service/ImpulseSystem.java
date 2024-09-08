package com.example.budget.domain.trade.service;

import com.example.budget.domain.trade.model.Signal;
import org.ta4j.core.num.Num;

public interface ImpulseSystem {
    Signal getSignal(Num maSlope, Num macdSlope);
}
