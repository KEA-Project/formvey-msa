package com.kale.responseservice.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetChoiceRes {
    private Long id;

    private Long questionId;

    private int choiceIndex;

    private String choiceContent;
}
