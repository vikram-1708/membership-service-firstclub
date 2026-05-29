package com.firstclub.membership.service;

import com.firstclub.membership.domain.records.Benefit;
import com.firstclub.membership.domain.entities.MembershipPlan;
import com.firstclub.membership.domain.entities.MembershipTier;
import com.firstclub.membership.domain.records.TierEligibilityRule;
import com.firstclub.membership.domain.entities.UserSubscription;
import com.firstclub.membership.dto.responses.BenefitResponse;
import com.firstclub.membership.dto.responses.MembershipPlanResponse;
import com.firstclub.membership.dto.responses.MembershipTierResponse;
import com.firstclub.membership.dto.responses.TierEligibilityResponse;
import com.firstclub.membership.dto.responses.UserMembershipResponse;
import org.springframework.stereotype.Component;

@Component
public class MembershipMapper {

    public MembershipPlanResponse toPlanResponse(MembershipPlan plan) {
        return new MembershipPlanResponse(
                plan.getId(),
                plan.getType(),
                plan.getDisplayName(),
                plan.getPrice(),
                plan.getDuration().toString()
        );
    }

    public MembershipTierResponse toTierResponse(MembershipTier tier) {
        TierEligibilityRule rule = tier.getEligibilityRule();
        return new MembershipTierResponse(
                tier.getId(),
                tier.getCode(),
                tier.getDisplayName(),
                tier.getRank(),
                new TierEligibilityResponse(
                        rule.minimumMonthlyOrders(),
                        rule.minimumMonthlyOrderValue(),
                        rule.eligibleCohorts()
                ),
                tier.getBenefits().stream().map(this::toBenefitResponse).toList()
        );
    }

    public UserMembershipResponse toUserMembershipResponse(
            UserSubscription subscription,
            MembershipPlan plan,
            MembershipTier tier
    ) {
        return new UserMembershipResponse(
                subscription.getId(),
                subscription.getUserId(),
                toPlanResponse(plan),
                toTierResponse(tier),
                subscription.getStatus(),
                subscription.getStartDate(),
                subscription.getExpiryDate(),
                subscription.isActive(),
                subscription.getCreatedAt(),
                subscription.getUpdatedAt()
        );
    }

    private BenefitResponse toBenefitResponse(Benefit benefit) {
        return new BenefitResponse(benefit.type(), benefit.description(), benefit.attributes());
    }
}
