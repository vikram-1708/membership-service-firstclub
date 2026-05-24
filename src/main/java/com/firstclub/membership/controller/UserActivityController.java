package com.firstclub.membership.controller;

import com.firstclub.membership.dto.responses.OrderResponse;
import com.firstclub.membership.dto.requests.RecordOrderRequest;
import com.firstclub.membership.dto.responses.TierRecommendationResponse;
import com.firstclub.membership.dto.requests.UpdateCohortsRequest;
import com.firstclub.membership.dto.responses.UserProfileResponse;
import com.firstclub.membership.service.OrderService;
import com.firstclub.membership.service.TierEvaluationService;
import com.firstclub.membership.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{userId}")
@RequiredArgsConstructor
public class UserActivityController {
    private final OrderService orderService;
    private final UserProfileService userProfileService;
    private final TierEvaluationService tierEvaluationService;

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse recordOrder(
            @PathVariable String userId,
            @Valid @RequestBody RecordOrderRequest request
    ) {
        return orderService.recordOrder(userId, request);
    }

    @PutMapping("/cohorts")
    public UserProfileResponse updateCohorts(
            @PathVariable String userId,
            @Valid @RequestBody UpdateCohortsRequest request
    ) {
        return userProfileService.updateCohorts(userId, request);
    }

    @GetMapping("/tier-recommendation")
    public TierRecommendationResponse getTierRecommendation(@PathVariable String userId) {
        return tierEvaluationService.getRecommendation(userId);
    }
}
