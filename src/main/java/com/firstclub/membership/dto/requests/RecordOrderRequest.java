package com.firstclub.membership.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RecordOrderRequest(
        @NotNull @DecimalMin(value = "0.01") BigDecimal orderValue
) {
}
