package com.kale.surveyservice.repository;

import com.kale.surveyservice.domain.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    List<Choice> findByQuestionId(Long id);
}
