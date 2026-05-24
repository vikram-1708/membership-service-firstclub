package com.firstclub.membership.domain.entities;

import com.firstclub.membership.domain.enums.PlanType;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Period;

@Getter
@RequiredArgsConstructor
public final class MembershipPlan {
    @NonNull
    private final String id;
    @NonNull
    private final PlanType type;
    @NonNull
    private final String displayName;
    @NonNull
    private final BigDecimal price;
    @NonNull
    private final Period duration;
}
