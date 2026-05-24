package com.firstclub.membership.config;

import com.firstclub.membership.domain.records.Benefit;
import com.firstclub.membership.domain.enums.BenefitType;
import com.firstclub.membership.domain.entities.MembershipPlan;
import com.firstclub.membership.domain.entities.MembershipTier;
import com.firstclub.membership.domain.enums.PlanType;
import com.firstclub.membership.domain.records.TierEligibilityRule;
import com.firstclub.membership.repository.interfaces.MembershipPlanRepository;
import com.firstclub.membership.repository.interfaces.MembershipTierRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class MembershipSeedData {

    @Bean
    CommandLineRunner seedMembershipCatalog(
            MembershipPlanRepository planRepository,
            MembershipTierRepository tierRepository
    ) {
        return args -> {
            seedPlans(planRepository);
            seedTiers(tierRepository);
        };
    }

    private void seedPlans(MembershipPlanRepository planRepository) {
        planRepository.save(new MembershipPlan(
                "1",
                PlanType.MONTHLY,
                "Monthly Membership",
                BigDecimal.valueOf(199),
                Period.ofMonths(1)
        ));
        planRepository.save(new MembershipPlan(
                "2",
                PlanType.QUARTERLY,
                "Quarterly Membership",
                BigDecimal.valueOf(499),
                Period.ofMonths(3)
        ));
        planRepository.save(new MembershipPlan(
                "3",
                PlanType.YEARLY,
                "Yearly Membership",
                BigDecimal.valueOf(1499),
                Period.ofYears(1)
        ));
    }

    private void seedTiers(MembershipTierRepository tierRepository) {
        tierRepository.save(new MembershipTier(
                "1",
                "SILVER",
                "Silver",
                1,
                new TierEligibilityRule(0, BigDecimal.ZERO, Set.of()),
                List.of(
                        new Benefit(BenefitType.FREE_DELIVERY, "Free delivery on eligible orders", Map.of("minimumOrderValue", "499")),
                        new Benefit(BenefitType.EXTRA_DISCOUNT, "Extra discount on selected categories", Map.of("discountPercent", "5"))
                )
        ));
        tierRepository.save(new MembershipTier(
                "2",
                "GOLD",
                "Gold",
                2,
                new TierEligibilityRule(5, BigDecimal.valueOf(5000), Set.of()),
                List.of(
                        new Benefit(BenefitType.FREE_DELIVERY, "Free delivery on eligible orders", Map.of("minimumOrderValue", "299")),
                        new Benefit(BenefitType.EXTRA_DISCOUNT, "Extra discount on selected categories", Map.of("discountPercent", "10")),
                        new Benefit(BenefitType.EARLY_SALE_ACCESS, "Early access to sale events", Map.of("hoursBeforePublicSale", "12"))
                )
        ));
        tierRepository.save(new MembershipTier(
                "3",
                "PLATINUM",
                "Platinum",
                3,
                new TierEligibilityRule(10, BigDecimal.valueOf(15000), Set.of("VIP", "INFLUENCER")),
                List.of(
                        new Benefit(BenefitType.FREE_DELIVERY, "Free delivery on all eligible orders", Map.of("minimumOrderValue", "0")),
                        new Benefit(BenefitType.EXTRA_DISCOUNT, "Highest discount on selected categories", Map.of("discountPercent", "15")),
                        new Benefit(BenefitType.EXCLUSIVE_DEALS, "Access to member-only deals", Map.of("dealVisibility", "exclusive")),
                        new Benefit(BenefitType.PRIORITY_SUPPORT, "Priority support for premium members", Map.of("slaHours", "4")),
                        new Benefit(BenefitType.EXCLUSIVE_COUPONS, "Exclusive coupon drops", Map.of("monthlyCoupons", "3"))
                )
        ));
    }
}
