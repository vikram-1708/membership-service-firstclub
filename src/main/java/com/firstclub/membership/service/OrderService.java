package com.firstclub.membership.service;

import com.firstclub.membership.domain.entities.MembershipTier;
import com.firstclub.membership.domain.entities.UserSubscription;
import com.firstclub.membership.domain.records.OrderRecord;
import com.firstclub.membership.dto.requests.RecordOrderRequest;
import com.firstclub.membership.dto.responses.OrderResponse;
import com.firstclub.membership.exception.ResourceNotFoundException;
import com.firstclub.membership.repository.interfaces.OrderRepository;
import com.firstclub.membership.repository.interfaces.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final TierEvaluationService tierEvaluationService;
    private final MembershipMapper mapper;
    private final Clock clock;

    public OrderResponse recordOrder(String userId, RecordOrderRequest request) {
        OrderRecord order = orderRepository.save(OrderRecord.create(userId, request.orderValue(), Instant.now(clock)));
        MembershipTier eligibleTier = tierEvaluationService.recommendTier(userId);
        updateActiveSubscriptionTier(userId, eligibleTier);
        return new OrderResponse(
                order.id(),
                order.userId(),
                order.orderValue(),
                order.orderedAt(),
                mapper.toTierResponse(eligibleTier)
        );
    }

    private void updateActiveSubscriptionTier(String userId, MembershipTier eligibleTier) {
        UserSubscription subscription = subscriptionRepository.findActiveByUserId(userId)
                .filter(activeSubscription -> activeSubscription.isActive(clock))
                .orElseThrow(() -> new ResourceNotFoundException("No active subscription found for user: " + userId));
        subscription.changeTier(eligibleTier, clock);
        subscriptionRepository.save(subscription);
    }
}
