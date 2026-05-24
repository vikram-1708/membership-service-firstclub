package com.firstclub.membership.service.eligibility;

import com.firstclub.membership.domain.entities.MembershipTier;

public interface TierEligibilityPolicy {
    boolean isEligible(MembershipTier tier, UserMonthlyMetrics metrics);
}
