package com.firstclub.membership.repository.interfaces;

import com.firstclub.membership.domain.entities.MembershipPlan;

import java.util.List;
import java.util.Optional;

public interface MembershipPlanRepository {
    List<MembershipPlan> findAll();

    Optional<MembershipPlan> findById(String id);

    MembershipPlan save(MembershipPlan plan);
}
