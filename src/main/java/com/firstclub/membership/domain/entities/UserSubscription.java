package com.firstclub.membership.domain.entities;

import com.firstclub.membership.domain.enums.SubscriptionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Clock;
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

    public static UserSubscription create(String userId, MembershipPlan plan, MembershipTier tier, Clock clock) {
        LocalDate startDate = LocalDate.now(clock);
        Instant now = Instant.now(clock);
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

    public void changeTier(MembershipTier newTier, Clock clock) {
        ensureActive(clock);
        this.tierId = newTier.getId();
        touch(clock);
    }

    public void renewPlan(MembershipPlan newPlan, Clock clock) {
        ensureActive(clock);
        this.planId = newPlan.getId();
        this.expiryDate = LocalDate.now(clock).plus(newPlan.getDuration());
        touch(clock);
    }

    public void cancel(Clock clock) {
        ensureActive(clock);
        this.status = SubscriptionStatus.CANCELLED;
        touch(clock);
    }

    public boolean isActive(Clock clock) {
        return status == SubscriptionStatus.ACTIVE && !expiryDate.isBefore(LocalDate.now(clock));
    }

    private void ensureActive(Clock clock) {
        if (!isActive(clock)) {
            throw new IllegalStateException("Subscription is not active");
        }
    }

    private void touch(Clock clock) {
        this.updatedAt = Instant.now(clock);
    }
}
