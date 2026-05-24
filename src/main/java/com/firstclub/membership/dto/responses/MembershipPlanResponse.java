package com.firstclub.membership.dto.responses;

import com.firstclub.membership.domain.enums.PlanType;

import java.math.BigDecimal;

public record MembershipPlanResponse(
        String id,
        PlanType type,
        String displayName,
        BigDecimal price,
        String duration
) {
}
