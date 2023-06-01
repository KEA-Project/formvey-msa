package com.kale.responseservice.repository;

import com.kale.responseservice.domain.ShortAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShortAnswerRepository extends JpaRepository<ShortAnswer, Long> {
    List<ShortAnswer> findByMemberId(Long memberId);
}
