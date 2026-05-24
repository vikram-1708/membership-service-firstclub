package com.firstclub.membership.service.eligibility;

import java.math.BigDecimal;
import java.util.Set;

public record UserMonthlyMetrics(
        int orderCount,
        BigDecimal totalOrderValue,
        Set<String> cohorts
) {
}
