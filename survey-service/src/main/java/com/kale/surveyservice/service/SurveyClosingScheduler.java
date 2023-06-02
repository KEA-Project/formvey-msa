package com.kale.surveyservice.service;

import com.kale.surveyservice.domain.Survey;
import com.kale.surveyservice.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyClosingScheduler {
    private final SurveyRepository surveyRepository;

    @Scheduled(fixedDelay = 60000) // 1분에 한번씩 마감 설문 검토
    public void closeSurveys() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Survey> surveys = surveyRepository.findAllByEndDateBefore(currentDateTime);

        for (Survey survey : surveys) {
            if (survey.getStatus() != 3)
                survey.setStatus(3); // status를 3으로 변경하여 설문 마감 처리
        }
        surveyRepository.saveAll(surveys); // 변경된 설문 상태를 저장
    }
}
