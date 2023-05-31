package com.kale.surveyservice.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetSurveyListRes {
    private Long surveyId;
    private String surveyTitle;
    private String surveyContent;
    private String endDate;
    private int dDay;
    private int responseCnt;
    private int status;
}
