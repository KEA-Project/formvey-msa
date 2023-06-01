package com.kale.responseservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultipleChoiceInfo {
    int choiceIndex;

    String choiceContent;

    int multipleChoiceCnt;
}
