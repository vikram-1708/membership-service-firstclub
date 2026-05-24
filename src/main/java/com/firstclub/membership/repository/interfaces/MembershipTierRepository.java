package com.firstclub.membership.repository.interfaces;

import com.firstclub.membership.domain.entities.MembershipTier;

import java.util.List;
import java.util.Optional;

public interface MembershipTierRepository {
    List<MembershipTier> findAll();

    Optional<MembershipTier> findById(String id);

    MembershipTier save(MembershipTier tier);
}
