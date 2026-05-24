package com.firstclub.membership.dto.responses;

import java.util.Set;

public record UserProfileResponse(
        String userId,
        Set<String> cohorts
) {
}
