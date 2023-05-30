package com.kale.surveyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetShortResponseListRes {
    private Long shortAnswerId;

    private Long shortFormId;
}
