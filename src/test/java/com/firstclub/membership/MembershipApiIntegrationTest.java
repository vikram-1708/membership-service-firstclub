package com.firstclub.membership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MembershipApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeSeededPlansAndTiers() throws Exception {
        mockMvc.perform(get("/api/v1/membership/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(get("/api/v1/membership/tiers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].code").value("SILVER"));
    }

    @Test
    void shouldManageSubscriptionLifecycle() throws Exception {
        String userId = "12345";

        mockMvc.perform(post("/api/v1/users/{userId}/subscription", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "planId": "1"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.tier.code").value("SILVER"));

        mockMvc.perform(delete("/api/v1/users/{userId}/subscription", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.cancelled").value(true))
                .andExpect(jsonPath("$.message").value("Subscription cancelled successfully"));

        mockMvc.perform(get("/api/v1/users/{userId}/subscription", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No active subscription found for user: " + userId));
    }

    @Test
    void shouldReturnCustomMessageWhenSubscriptionIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userId}/subscription", "missing-user"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No active subscription found for user: missing-user"))
                .andExpect(jsonPath("$.path").value("/api/v1/users/missing-user/subscription"));
    }

    @Test
    void shouldReturnCustomMessageWhenUserAlreadyHasActiveSubscription() throws Exception {
        String userId = "duplicate-subscription-user";

        mockMvc.perform(post("/api/v1/users/{userId}/subscription", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "planId": "1"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/users/{userId}/subscription", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "planId": "1"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("User already has an active subscription"));
    }

    @Test
    void shouldReturnValidationDetailsForBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/users/{userId}/subscription", "validation-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "planId": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.details[0]").value("planId must not be blank"));
    }

    @Test
    void shouldRecommendGoldTierFromMonthlyOrderMetrics() throws Exception {
        String userId = "gold-recommendation-user";
        subscribeUser(userId);

        for (int index = 0; index < 5; index++) {
            mockMvc.perform(post("/api/v1/users/{userId}/orders", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "orderValue": 1000
                                    }
                                    """))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/v1/users/{userId}/tier-recommendation", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyOrderCount").value(5))
                .andExpect(jsonPath("$.monthlyOrderValue").value(5000))
                .andExpect(jsonPath("$.recommendedTier.code").value("GOLD"));
    }

    @Test
    void shouldRecommendPlatinumWhenVipMeetsOrderCriteria() throws Exception {
        String userId = "platinum-recommendation-user";
        subscribeUser(userId);

        mockMvc.perform(put("/api/v1/users/{userId}/cohorts", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cohorts": ["VIP"]
                                }
                                """))
                .andExpect(status().isOk());

        for (int index = 0; index < 10; index++) {
            mockMvc.perform(post("/api/v1/users/{userId}/orders", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "orderValue": 1500
                                    }
                                    """))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/v1/users/{userId}/tier-recommendation", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendedTier.code").value("PLATINUM"));
    }

    @Test
    void shouldNotRecommendTierWhenUserHasNoActiveSubscription() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userId}/tier-recommendation", "unsubscribed-user"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No active subscription found for user: unsubscribed-user"));
    }

    private void subscribeUser(String userId) throws Exception {
        mockMvc.perform(post("/api/v1/users/{userId}/subscription", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "planId": "1"
                                }
                                """))
                .andExpect(status().isCreated());
    }
}
