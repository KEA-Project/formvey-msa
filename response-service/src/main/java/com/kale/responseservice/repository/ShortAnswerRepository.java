package com.kale.responseservice.repository;

import com.kale.responseservice.domain.ShortAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShortAnswerRepository extends JpaRepository<ShortAnswer, Long> {
    @Query("SELECT sa FROM ShortAnswer sa where sa.memberId=:memberId")
    List<ShortAnswer> findByMemberId(Long memberId);

    @Query("SELECT sa FROM ShortAnswer sa WHERE sa.memberId=:memberId and sa.shortFormId=:shortFormId")
    List<ShortAnswer> findExistById(Long memberId, Long shortFormId);
}
