package com.firstclub.membership.repository.interfaces;

import com.firstclub.membership.domain.records.OrderRecord;

import java.time.Instant;
import java.util.List;

public interface OrderRepository {
    OrderRecord save(OrderRecord orderRecord);

    List<OrderRecord> findByUserIdAndOrderedAtBetween(String userId, Instant fromInclusive, Instant toExclusive);
}
