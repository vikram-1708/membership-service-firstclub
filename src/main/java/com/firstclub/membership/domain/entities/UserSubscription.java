package com.firstclub.membership.domain.entities;

import com.firstclub.membership.domain.enums.SubscriptionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSubscription {
    @NonNull
    private final String id;
    @NonNull
    private final String userId;
    @NonNull
    private String planId;
    @NonNull
    private String tierId;
    @NonNull
    private SubscriptionStatus status;
    @NonNull
    private final LocalDate startDate;
    @NonNull
    private LocalDate expiryDate;
    @NonNull
    private final Instant createdAt;
    @NonNull
    private Instant updatedAt;

    public static UserSubscription create(String userId, MembershipPlan plan, MembershipTier tier) {
        LocalDate startDate = LocalDate.now();
        Instant now = Instant.now();
        return new UserSubscription(
                UUID.randomUUID().toString(),
                userId,
                plan.getId(),
                tier.getId(),
                SubscriptionStatus.ACTIVE,
                startDate,
                startDate.plus(plan.getDuration()),
                now,
                now
        );
    }

    public void changeTier(MembershipTier newTier) {
        ensureActive();
        this.tierId = newTier.getId();
        touch();
    }

    public void renewPlan(MembershipPlan newPlan) {
        ensureActive();
        this.planId = newPlan.getId();
        this.expiryDate = LocalDate.now().plus(newPlan.getDuration());
        touch();
    }

    public void cancel() {
        ensureActive();
        this.status = SubscriptionStatus.CANCELLED;
        touch();
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE && !expiryDate.isBefore(LocalDate.now());
    }

    private void ensureActive() {
        if (!isActive()) {
            throw new IllegalStateException("Subscription is not active");
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }
}
