package com.kale.surveyservice.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetSurveyList {

    private List<GetSurveyListRes> getSurveyListRes = new ArrayList<>();

    private int totalPageCnt;

    private int unReleasedPageCnt;

    private int releasedPageCnt;

    private int closedPageCnt;
}
