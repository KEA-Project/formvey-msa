package com.kale.surveyservice.repository;

import com.kale.surveyservice.domain.UserReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRewardRepository extends JpaRepository<UserReward, Long> {
    @Query("SELECT r FROM UserReward r where r.memberId=:id")
    List<UserReward> findByMemberId(Long id);
}
