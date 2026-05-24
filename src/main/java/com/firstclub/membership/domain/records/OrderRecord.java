package com.firstclub.membership.domain.records;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record OrderRecord(
        String id,
        String userId,
        BigDecimal orderValue,
        Instant orderedAt
) {
    public static OrderRecord create(String userId, BigDecimal orderValue, Instant orderedAt) {
        return new OrderRecord(UUID.randomUUID().toString(), Objects.requireNonNull(userId), orderValue, orderedAt);
    }
}
