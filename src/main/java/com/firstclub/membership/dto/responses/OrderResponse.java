package com.firstclub.membership.dto.responses;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        String id,
        String userId,
        BigDecimal orderValue,
        Instant orderedAt,
        MembershipTierResponse eligibleTier
) {
}
