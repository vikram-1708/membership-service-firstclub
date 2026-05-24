package com.firstclub.membership.dto.requests;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateCohortsRequest(
        @NotNull Set<String> cohorts
) {
}
