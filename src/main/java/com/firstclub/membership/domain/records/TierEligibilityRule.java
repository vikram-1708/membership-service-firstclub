package com.firstclub.membership.domain.records;

import java.math.BigDecimal;
import java.util.Set;

public record TierEligibilityRule(
        int minimumMonthlyOrders,
        BigDecimal minimumMonthlyOrderValue,
        Set<String> eligibleCohorts
) {

    public boolean isCohortRestricted() {
        return eligibleCohorts != null && !eligibleCohorts.isEmpty();
    }
}
