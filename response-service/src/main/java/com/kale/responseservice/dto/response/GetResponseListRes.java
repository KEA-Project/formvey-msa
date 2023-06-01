package com.kale.responseservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetResponseListRes {
    private Long surveyId;

    private Long responseId;

    private String surveyTitle;

    private String surveyContent;

    private String endDate;

    private int dDay;

    private int status;
}
