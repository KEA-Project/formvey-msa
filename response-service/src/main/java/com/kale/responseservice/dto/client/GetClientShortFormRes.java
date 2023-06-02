package com.kale.responseservice.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetClientShortFormRes {
    private Long id;

    private Long surveyId;

    private Long memberId;

    private String shortQuestion;

    private int shortType;

    private int shortResponse;
}
