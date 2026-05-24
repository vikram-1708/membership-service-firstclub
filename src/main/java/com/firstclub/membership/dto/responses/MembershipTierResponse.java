package com.firstclub.membership.dto.responses;

import java.util.List;

public record MembershipTierResponse(
        String id,
        String code,
        String displayName,
        int rank,
        TierEligibilityResponse eligibility,
        List<BenefitResponse> benefits
) {
}
