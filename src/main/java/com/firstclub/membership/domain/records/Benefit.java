package com.firstclub.membership.domain.records;

import com.firstclub.membership.domain.enums.BenefitType;

import java.util.Map;

public record Benefit(
        BenefitType type,
        String description,
        Map<String, String> attributes
) {
}
