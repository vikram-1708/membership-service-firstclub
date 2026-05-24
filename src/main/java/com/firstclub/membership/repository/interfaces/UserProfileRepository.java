package com.firstclub.membership.repository.interfaces;

import com.firstclub.membership.domain.records.UserProfile;

import java.util.Optional;

public interface UserProfileRepository {
    Optional<UserProfile> findByUserId(String userId);

    UserProfile save(UserProfile userProfile);
}
