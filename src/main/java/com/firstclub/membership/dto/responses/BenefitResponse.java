package com.firstclub.membership.dto.responses;

import com.firstclub.membership.domain.enums.BenefitType;

import java.util.Map;

public record BenefitResponse(
        BenefitType type,
        String description,
        Map<String, String> attributes
) {
}
