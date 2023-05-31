package com.kale.surveyservice.dto.shortOption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetShortOptionRes {
    private Long shortFormId;

    private int shortIndex;

    private String shortContent;
}
