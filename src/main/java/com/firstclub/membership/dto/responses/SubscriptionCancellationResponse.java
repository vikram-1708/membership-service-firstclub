package com.firstclub.membership.dto.responses;

public record SubscriptionCancellationResponse(
        String userId,
        boolean cancelled,
        String message
) {
}
