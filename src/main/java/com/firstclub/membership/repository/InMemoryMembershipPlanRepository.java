package com.firstclub.membership.repository;

import com.firstclub.membership.domain.entities.MembershipPlan;
import com.firstclub.membership.repository.interfaces.MembershipPlanRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMembershipPlanRepository implements MembershipPlanRepository {
    private final ConcurrentMap<String, MembershipPlan> plans = new ConcurrentHashMap<>();

    @Override
    public List<MembershipPlan> findAll() {
        return plans.values().stream()
                .sorted(Comparator.comparing(MembershipPlan::getPrice))
                .toList();
    }

    @Override
    public Optional<MembershipPlan> findById(String id) {
        return Optional.ofNullable(plans.get(id));
    }

    @Override
    public MembershipPlan save(MembershipPlan plan) {
        plans.put(plan.getId(), plan);
        return plan;
    }
}
