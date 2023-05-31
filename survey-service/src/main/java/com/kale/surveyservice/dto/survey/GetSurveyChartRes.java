package com.kale.surveyservice.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetSurveyChartRes {
    private int createSurveyCnt;

    private int responseCnt;

    private int shortFormResponseCnt;

    private int unReleasedSurveyCnt;

    private int releasedSurveyCnt;

    private int closedSurveyCnt;
}
