package com.firstclub.membership.repository.interfaces;

import com.firstclub.membership.domain.entities.UserSubscription;

import java.util.Optional;

public interface UserSubscriptionRepository {
    Optional<UserSubscription> findActiveByUserId(String userId);

    Optional<UserSubscription> findLatestByUserId(String userId);

    UserSubscription save(UserSubscription subscription);
}
