package com.kale.responseservice.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
