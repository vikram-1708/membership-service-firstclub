package com.firstclub.membership.service.eligibility;

import com.firstclub.membership.domain.entities.MembershipTier;
import com.firstclub.membership.domain.records.TierEligibilityRule;
import org.springframework.stereotype.Component;

@Component
public class DefaultTierEligibilityPolicy implements TierEligibilityPolicy {

    @Override
    public boolean isEligible(MembershipTier tier, UserMonthlyMetrics metrics) {
        TierEligibilityRule rule = tier.getEligibilityRule();
        boolean hasEnoughOrders = metrics.orderCount() >= rule.minimumMonthlyOrders();
        boolean hasEnoughValue = metrics.totalOrderValue().compareTo(rule.minimumMonthlyOrderValue()) >= 0;
        boolean cohortMatches = !rule.isCohortRestricted()
                || metrics.cohorts().stream().anyMatch(rule.eligibleCohorts()::contains);
        return hasEnoughOrders && hasEnoughValue &&  cohortMatches;
    }
}
