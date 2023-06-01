package com.kale.surveyservice.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetSurveyRes {
    private Long id;

    private Long memberId;

    private String surveyTitle;

    private String surveyContent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int responseCnt;

    private int isAnonymous;

    private int isPublic;

    private String exitUrl;

    private int status;
}
