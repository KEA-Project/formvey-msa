package com.kale.responseservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetResponseIndividualRes {
    private Long responseId;

    private Long memberId;

    private String nickname;

    private String responseDate;

    private int pages;
}
