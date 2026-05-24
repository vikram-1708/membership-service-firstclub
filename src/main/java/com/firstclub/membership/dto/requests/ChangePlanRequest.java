package com.firstclub.membership.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record ChangePlanRequest(
        @NotBlank String planId
) {
}
