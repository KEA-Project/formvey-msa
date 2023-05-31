package com.kale.surveyservice.dto.question;

import com.kale.surveyservice.domain.Question;
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
public class PostQuestionReq {
    private int questionIdx;

    private String questionTitle;

    private int type;

    private List<PostChoiceReq> choices = new ArrayList<>();

    private int isEssential;

    private int isShort;

    //-----------------------------------------------------------------------
    public static Question toEntity(Survey survey, PostQuestionReq dto){
        return Question.builder()
                .survey(survey)
                .questionIdx(dto.questionIdx)
                .questionTitle(dto.questionTitle)
                .type(dto.type)
                .isShort(dto.isShort)
                .isEssential(dto.isEssential)
                .build();
    }
}
