package com.firstclub.membership.dto.responses;

import com.firstclub.membership.domain.enums.SubscriptionStatus;

import java.time.Instant;
import java.time.LocalDate;

public record UserMembershipResponse(
        String subscriptionId,
        String userId,
        MembershipPlanResponse plan,
        MembershipTierResponse tier,
        SubscriptionStatus status,
        LocalDate startDate,
        LocalDate expiryDate,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
