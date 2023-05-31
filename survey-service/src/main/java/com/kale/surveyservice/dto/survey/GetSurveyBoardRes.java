package com.kale.surveyservice.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetSurveyBoardRes {
    private Long surveyId;
    private Long memberId;
    private String surveyTitle;
    private int dDay;
    private int responseCnt;
    private String nickname;
    private int pages;
}
