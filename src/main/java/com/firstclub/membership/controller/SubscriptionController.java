package com.firstclub.membership.controller;

import com.firstclub.membership.dto.requests.ChangePlanRequest;
import com.firstclub.membership.dto.requests.SubscribeRequest;
import com.firstclub.membership.dto.responses.SubscriptionCancellationResponse;
import com.firstclub.membership.dto.responses.UserMembershipResponse;
import com.firstclub.membership.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{userId}/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserMembershipResponse subscribe(
            @PathVariable String userId,
            @Valid @RequestBody SubscribeRequest request
    ) {
        return subscriptionService.subscribe(userId, request);
    }

    @GetMapping
    public UserMembershipResponse getCurrentMembership(@PathVariable String userId) {
        return subscriptionService.getCurrentMembership(userId);
    }

    @PatchMapping("/plan")
    public UserMembershipResponse changePlan(
            @PathVariable String userId,
            @Valid @RequestBody ChangePlanRequest request
    ) {
        return subscriptionService.changePlan(userId, request);
    }

    @DeleteMapping
    public SubscriptionCancellationResponse cancel(@PathVariable String userId) {
        return subscriptionService.cancel(userId);
    }
}
