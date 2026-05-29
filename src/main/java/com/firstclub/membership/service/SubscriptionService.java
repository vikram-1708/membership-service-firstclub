package com.firstclub.membership.service;

import com.firstclub.membership.domain.entities.MembershipPlan;
import com.firstclub.membership.domain.entities.MembershipTier;
import com.firstclub.membership.domain.entities.UserSubscription;
import com.firstclub.membership.dto.requests.ChangePlanRequest;
import com.firstclub.membership.dto.requests.SubscribeRequest;
import com.firstclub.membership.dto.responses.SubscriptionCancellationResponse;
import com.firstclub.membership.dto.responses.UserMembershipResponse;
import com.firstclub.membership.exception.BusinessRuleViolationException;
import com.firstclub.membership.exception.ResourceNotFoundException;
import com.firstclub.membership.repository.interfaces.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserSubscriptionRepository subscriptionRepository;
    private final MembershipCatalogService catalogService;
    private final MembershipMapper mapper;

    public UserMembershipResponse subscribe(String userId, SubscribeRequest request) {
        subscriptionRepository.findLatestByUserId(userId)
                .filter(UserSubscription::isActive)
                .ifPresent(subscription -> {
                    throw new BusinessRuleViolationException("User already has an active subscription");
                });
        MembershipPlan plan = catalogService.getPlanById(request.planId());
        MembershipTier tier = catalogService.getDefaultTier();
        UserSubscription subscription = subscriptionRepository.save(UserSubscription.create(userId, plan, tier));
        return mapper.toUserMembershipResponse(subscription, plan, tier);
    }

    public UserMembershipResponse getCurrentMembership(String userId) {
        UserSubscription subscription = getActiveSubscription(userId);
        return toResponse(subscription);
    }

    public UserMembershipResponse changePlan(String userId, ChangePlanRequest request) {
        UserSubscription subscription = getActiveSubscription(userId);
        MembershipPlan plan = catalogService.getPlanById(request.planId());
        subscription.renewPlan(plan);
        subscriptionRepository.save(subscription);
        return toResponse(subscription);
    }

    public SubscriptionCancellationResponse cancel(String userId) {
        UserSubscription subscription = getActiveSubscription(userId);
        subscription.cancel();
        subscriptionRepository.save(subscription);
        return new SubscriptionCancellationResponse(
                userId,
                true,
                "Subscription cancelled successfully"
        );
    }

    private UserSubscription getActiveSubscription(String userId) {
        return subscriptionRepository.findLatestByUserId(userId)
                .filter(UserSubscription::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("No active subscription found for user: " + userId));
    }

    private UserMembershipResponse toResponse(UserSubscription subscription) {
        MembershipPlan plan = catalogService.getPlanById(subscription.getPlanId());
        MembershipTier tier = catalogService.getTierById(subscription.getTierId());
        return mapper.toUserMembershipResponse(subscription, plan, tier);
    }
}
