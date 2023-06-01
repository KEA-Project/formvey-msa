package com.kale.surveyservice.dto.client;

import com.kale.surveyservice.domain.Question;
import jakarta.persistence.*;
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
