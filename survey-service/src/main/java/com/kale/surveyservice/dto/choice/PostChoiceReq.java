package com.kale.surveyservice.dto.choice;

import com.kale.surveyservice.domain.Choice;
import com.kale.surveyservice.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostChoiceReq {
    private int choiceIdx;

    private String choiceContent;

    public static Choice toEntity(Question question, PostChoiceReq dto){
        return Choice.builder()
                .question(question)
                .choiceIndex(dto.choiceIdx)
                .choiceContent(dto.choiceContent)
                .build();
    }
}
