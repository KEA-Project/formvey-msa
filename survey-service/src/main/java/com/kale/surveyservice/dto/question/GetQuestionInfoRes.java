package com.kale.surveyservice.dto.question;

import com.kale.surveyservice.dto.choice.GetChoiceInfoRes;
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
public class GetQuestionInfoRes {

    private Long questionId;

    private int questionIdx;

    private String questionTitle;

    private int type;

    private int isEssential;

    private int isShort;

    private List<GetChoiceInfoRes> choices = new ArrayList<>();

}
