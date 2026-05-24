package com.firstclub.membership.service;

import com.firstclub.membership.domain.records.UserProfile;
import com.firstclub.membership.dto.requests.UpdateCohortsRequest;
import com.firstclub.membership.dto.responses.UserProfileResponse;
import com.firstclub.membership.repository.interfaces.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfile getOrCreate(String userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseGet(() -> userProfileRepository.save(new UserProfile(userId, Set.of())));
    }

    public UserProfileResponse updateCohorts(String userId, UpdateCohortsRequest request) {
        UserProfile profile = userProfileRepository.save(new UserProfile(userId, Set.copyOf(request.cohorts())));
        return new UserProfileResponse(profile.userId(), profile.cohorts());
    }
}
