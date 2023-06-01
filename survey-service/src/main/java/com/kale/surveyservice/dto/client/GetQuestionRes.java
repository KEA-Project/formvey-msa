package com.kale.surveyservice.dto.client;

import com.kale.surveyservice.domain.Survey;
import com.kale.surveyservice.dto.choice.GetChoiceInfoRes;
import jakarta.persistence.*;
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
public class GetQuestionRes {
    private Long id;

    private Long surveyId;

    private int questionIdx;

    private String questionTitle;

    private int type;

    private int isEssential;

    private int isShort;

    private List<GetChoiceRes> choices = new ArrayList<>();
}
