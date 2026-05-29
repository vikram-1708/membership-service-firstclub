package com.firstclub.membership.repository;

import com.firstclub.membership.domain.entities.UserSubscription;
import com.firstclub.membership.repository.interfaces.UserSubscriptionRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryUserSubscriptionRepository implements UserSubscriptionRepository {
    private final ConcurrentMap<String, UserSubscription> latestByUserId = new ConcurrentHashMap<>();

    @Override
    public Optional<UserSubscription> findLatestByUserId(String userId) {
        return Optional.ofNullable(latestByUserId.get(userId));
    }

    @Override
    public UserSubscription save(UserSubscription subscription) {
        latestByUserId.put(subscription.getUserId(), subscription);
        return subscription;
    }

    public List<UserSubscription> findAllForDebugging() {
        return latestByUserId.values().stream()
                .sorted(Comparator.comparing(UserSubscription::getCreatedAt))
                .toList();
    }
}
