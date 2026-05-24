package com.firstclub.membership.domain.entities;

import com.firstclub.membership.domain.records.Benefit;
import com.firstclub.membership.domain.records.TierEligibilityRule;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;

@Getter
public final class MembershipTier {
    private final String id;
    private final String code;
    private final String displayName;
    private final int rank;
    private final TierEligibilityRule eligibilityRule;
    private final List<Benefit> benefits;

    public MembershipTier(
            @NonNull String id,
            @NonNull String code,
            @NonNull String displayName,
            int rank,
            @NonNull TierEligibilityRule eligibilityRule,
            @NonNull List<Benefit> benefits
    ) {
        this.id = Objects.requireNonNull(id);
        this.code = Objects.requireNonNull(code);
        this.displayName = Objects.requireNonNull(displayName);
        this.rank = rank;
        this.eligibilityRule = Objects.requireNonNull(eligibilityRule);
        this.benefits = List.copyOf(benefits);
    }
}
