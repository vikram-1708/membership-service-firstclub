package com.firstclub.membership.domain.records;

import java.util.Set;

public record UserProfile(
        String userId,
        Set<String> cohorts
) {
}
