package com.kale.surveyservice.dto.shortForm;

import com.kale.surveyservice.dto.shortOption.GetShortOptionRes;
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
public class GetShortFormRes {
    private Long surveyId;
    private String surveyTitle;
    private String shortQuestion;
    private int shortType;
    private List<GetShortOptionRes> options = new ArrayList<>();
}
