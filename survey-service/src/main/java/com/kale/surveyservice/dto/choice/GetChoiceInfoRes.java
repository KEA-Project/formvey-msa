package com.kale.surveyservice.dto.choice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetChoiceInfoRes {

    private Long choiceId;
    private int choiceIndex;
    private String choiceContent;

}
