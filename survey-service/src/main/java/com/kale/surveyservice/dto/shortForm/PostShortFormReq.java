package com.kale.surveyservice.dto.shortForm;

import com.kale.surveyservice.domain.ShortForm;
import com.kale.surveyservice.domain.Survey;
import com.kale.surveyservice.dto.choice.PostChoiceReq;
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
public class PostShortFormReq {

    private String shortQuestion;

    private int shortType;

    private int shortResponse;

    private List<PostChoiceReq> shortOptions = new ArrayList<>();
    //-------------------------------------------------------------

    public static ShortForm toEntity(Survey survey, PostShortFormReq dto){
        return ShortForm.builder()
                .survey(survey)
                .shortType(dto.shortType)
                .shortResponse(0)
                .shortQuestion(dto.shortQuestion)
                .build();
    }
}
