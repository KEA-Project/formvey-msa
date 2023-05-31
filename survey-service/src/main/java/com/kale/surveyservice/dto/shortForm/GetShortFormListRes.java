package com.kale.surveyservice.dto.shortForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetShortFormListRes {
    private Long surveyId;
    private String surveyTitle;
    private Long Id;
    private String shortQuestion;
    private int shortType;
    private int shortResponse;
    private int pages;
    private int shortResultStatus;
}
