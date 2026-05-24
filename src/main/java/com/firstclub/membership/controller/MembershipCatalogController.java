package com.firstclub.membership.controller;

import com.firstclub.membership.dto.responses.MembershipPlanResponse;
import com.firstclub.membership.dto.responses.MembershipTierResponse;
import com.firstclub.membership.service.MembershipCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/membership")
@RequiredArgsConstructor
public class MembershipCatalogController {
    private final MembershipCatalogService catalogService;

    @GetMapping("/plans")
    public List<MembershipPlanResponse> getPlans() {
        return catalogService.getPlans();
    }

    @GetMapping("/tiers")
    public List<MembershipTierResponse> getTiers() {
        return catalogService.getTiers();
    }
}
