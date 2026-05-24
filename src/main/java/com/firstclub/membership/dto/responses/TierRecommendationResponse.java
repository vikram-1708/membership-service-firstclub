package com.firstclub.membership.dto.responses;

import java.math.BigDecimal;
import java.util.Set;

public record TierRecommendationResponse(
        String userId,
        int monthlyOrderCount,
        BigDecimal monthlyOrderValue,
        Set<String> cohorts,
        MembershipTierResponse recommendedTier
) {
}
