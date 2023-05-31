package com.kale.responseservice.repository;

import com.kale.responseservice.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByResponseId(Long id);
    List<Answer> findByQuestionId(Long questionId);
}
