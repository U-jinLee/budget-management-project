package com.example.budget.global.util;

import com.example.budget.domain.trade.dto.KlineDto;

import java.math.BigDecimal;
import java.util.List;

public class ShortSafetyPositionUtil {

    private ShortSafetyPositionUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isPositiveClose(List<KlineDto> klines) {
        BigDecimal value1 = klines.get(199).getClosePrice().subtract(klines.get(198).getClosePrice());
        BigDecimal value2 = klines.get(198).getClosePrice().subtract(klines.get(197).getClosePrice());
        BigDecimal value3 = klines.get(197).getClosePrice().subtract(klines.get(196).getClosePrice());

        return value1.compareTo(BigDecimal.valueOf(0)) >= 0 &&
                value2.compareTo(BigDecimal.valueOf(0)) >= 0 &&
                value3.compareTo(BigDecimal.valueOf(0)) >= 0;
    }
}
