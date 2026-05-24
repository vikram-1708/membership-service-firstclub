package com.firstclub.membership.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record SubscribeRequest(
        @NotBlank String planId
) {
}
