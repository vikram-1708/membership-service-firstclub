package com.firstclub.membership.repository;

import com.firstclub.membership.domain.entities.MembershipTier;
import com.firstclub.membership.repository.interfaces.MembershipTierRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMembershipTierRepository implements MembershipTierRepository {
    private final ConcurrentMap<String, MembershipTier> tiers = new ConcurrentHashMap<>();

    @Override
    public List<MembershipTier> findAll() {
        return tiers.values().stream()
                .sorted(Comparator.comparingInt(MembershipTier::getRank))
                .toList();
    }

    @Override
    public Optional<MembershipTier> findById(String id) {
        return Optional.ofNullable(tiers.get(id));
    }

    @Override
    public MembershipTier save(MembershipTier tier) {
        tiers.put(tier.getId(), tier);
        return tier;
    }
}
