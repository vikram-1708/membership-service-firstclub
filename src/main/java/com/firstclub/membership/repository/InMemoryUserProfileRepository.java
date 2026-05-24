package com.firstclub.membership.repository;

import com.firstclub.membership.domain.records.UserProfile;
import com.firstclub.membership.repository.interfaces.UserProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryUserProfileRepository implements UserProfileRepository {
    private final ConcurrentMap<String, UserProfile> profiles = new ConcurrentHashMap<>();

    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        return Optional.ofNullable(profiles.get(userId));
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        profiles.put(userProfile.userId(), userProfile);
        return userProfile;
    }
}
