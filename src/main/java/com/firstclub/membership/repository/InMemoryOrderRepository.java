package com.firstclub.membership.repository;

import com.firstclub.membership.domain.records.OrderRecord;
import com.firstclub.membership.repository.interfaces.OrderRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryOrderRepository implements OrderRepository {
    private final List<OrderRecord> orders = new CopyOnWriteArrayList<>();

    @Override
    public OrderRecord save(OrderRecord orderRecord) {
        orders.add(orderRecord);
        return orderRecord;
    }

    @Override
    public List<OrderRecord> findByUserIdAndOrderedAtBetween(String userId, Instant fromInclusive, Instant toExclusive) {
        return orders.stream()
                .filter(order -> order.userId().equals(userId))
                .filter(order -> !order.orderedAt().isBefore(fromInclusive))
                .filter(order -> order.orderedAt().isBefore(toExclusive))
                .toList();
    }
}
