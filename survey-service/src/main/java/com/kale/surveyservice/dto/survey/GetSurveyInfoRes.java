package com.kale.surveyservice.dto.survey;

import com.kale.surveyservice.dto.question.GetQuestionInfoRes;
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
public class GetSurveyInfoRes {
    private Long memberId;
    private String surveyTitle;
    private String surveyContent;
    private String startDate;
    private String endDate;
    private int responseCnt;
    private int isAnonymous; // 0 -> 익명x, 1 -> 익명 가능
    private int isPublic;
    private String exitUrl;
    private int status;
    private List<GetQuestionInfoRes> questions = new ArrayList<>();
}
