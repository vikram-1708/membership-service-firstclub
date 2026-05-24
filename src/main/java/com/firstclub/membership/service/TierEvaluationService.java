package com.firstclub.membership.service;

import com.firstclub.membership.domain.entities.MembershipTier;
import com.firstclub.membership.domain.records.OrderRecord;
import com.firstclub.membership.domain.records.UserProfile;
import com.firstclub.membership.dto.responses.TierRecommendationResponse;
import com.firstclub.membership.exception.ResourceNotFoundException;
import com.firstclub.membership.repository.interfaces.MembershipTierRepository;
import com.firstclub.membership.repository.interfaces.OrderRepository;
import com.firstclub.membership.repository.interfaces.UserSubscriptionRepository;
import com.firstclub.membership.service.eligibility.TierEligibilityPolicy;
import com.firstclub.membership.service.eligibility.UserMonthlyMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TierEvaluationService {
    private final MembershipTierRepository tierRepository;
    private final OrderRepository orderRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final UserProfileService userProfileService;
    private final TierEligibilityPolicy eligibilityPolicy;
    private final MembershipMapper mapper;
    private final Clock clock;

    public MembershipTier recommendTier(String userId) {
        ensureUserHasActiveSubscription(userId);
        UserMonthlyMetrics metrics = calculateCurrentMonthMetrics(userId);
        return tierRepository.findAll().stream()
                .filter(tier -> eligibilityPolicy.isEligible(tier, metrics))
                .max(Comparator.comparingInt(MembershipTier::getRank))
                .orElseThrow(() -> new IllegalStateException("No eligible tier configured"));
    }

    public TierRecommendationResponse getRecommendation(String userId) {
        ensureUserHasActiveSubscription(userId);
        UserMonthlyMetrics metrics = calculateCurrentMonthMetrics(userId);
        MembershipTier recommendedTier = tierRepository.findAll().stream()
                .filter(tier -> eligibilityPolicy.isEligible(tier, metrics))
                .max(Comparator.comparingInt(MembershipTier::getRank))
                .orElseThrow(() -> new IllegalStateException("No eligible tier configured"));
        return new TierRecommendationResponse(
                userId,
                metrics.orderCount(),
                metrics.totalOrderValue(),
                metrics.cohorts(),
                mapper.toTierResponse(recommendedTier)
        );
    }

    private UserMonthlyMetrics calculateCurrentMonthMetrics(String userId) {
        ZoneId zone = clock.getZone();
        YearMonth currentMonth = YearMonth.now(clock);
        Instant from = currentMonth.atDay(1).atStartOfDay(zone).toInstant();
        Instant to = currentMonth.plusMonths(1).atDay(1).atStartOfDay(zone).toInstant();
        List<OrderRecord> orders = orderRepository.findByUserIdAndOrderedAtBetween(userId, from, to);
        BigDecimal totalValue = orders.stream()
                .map(OrderRecord::orderValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        UserProfile profile = userProfileService.getOrCreate(userId);
        return new UserMonthlyMetrics(orders.size(), totalValue, profile.cohorts());
    }

    private void ensureUserHasActiveSubscription(String userId) {
        subscriptionRepository.findLatestByUserId(userId)
                .filter(subscription -> subscription.isActive(clock))
                .orElseThrow(() -> new ResourceNotFoundException("No active subscription found for user: " + userId));
    }
}
