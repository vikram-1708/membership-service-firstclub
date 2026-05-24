package com.firstclub.membership.service;

import com.firstclub.membership.domain.entities.MembershipPlan;
import com.firstclub.membership.domain.entities.MembershipTier;
import com.firstclub.membership.dto.responses.MembershipPlanResponse;
import com.firstclub.membership.dto.responses.MembershipTierResponse;
import com.firstclub.membership.exception.ResourceNotFoundException;
import com.firstclub.membership.repository.interfaces.MembershipPlanRepository;
import com.firstclub.membership.repository.interfaces.MembershipTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipCatalogService {
    private static final String DEFAULT_TIER_CODE = "SILVER";

    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    private final MembershipMapper mapper;

    public List<MembershipPlanResponse> getPlans() {
        return planRepository.findAll().stream()
                .map(mapper::toPlanResponse)
                .toList();
    }

    public List<MembershipTierResponse> getTiers() {
        return tierRepository.findAll().stream()
                .map(mapper::toTierResponse)
                .toList();
    }

    public MembershipPlan getPlanById(String planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership plan not found: " + planId));
    }

    public MembershipTier getTierById(String tierId) {
        return tierRepository.findById(tierId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership tier not found: " + tierId));
    }

    public MembershipTier getDefaultTier() {
        return tierRepository.findAll().stream()
                .filter(tier -> DEFAULT_TIER_CODE.equals(tier.getCode()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Default membership tier is not configured"));
    }
}
