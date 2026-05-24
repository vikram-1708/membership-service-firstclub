package com.firstclub.membership.dto.responses;

import java.math.BigDecimal;
import java.util.Set;

public record TierEligibilityResponse(
        int minimumMonthlyOrders,
        BigDecimal minimumMonthlyOrderValue,
        Set<String> eligibleCohorts
) {
}
