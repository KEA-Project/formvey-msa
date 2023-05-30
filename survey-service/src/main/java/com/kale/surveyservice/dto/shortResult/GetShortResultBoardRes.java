package com.kale.surveyservice.dto.shortResult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetShortResultBoardRes {

    private Long surveyId;
    private String surveyTitle;
    private Long Id;
    private Long shortFormId;
    private String shortQuestion;
    private int shortType;
    private int shortResponse;

    private int pages;

}
