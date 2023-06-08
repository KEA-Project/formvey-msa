package com.kale.responseservice.dto.shortAnswer;

import com.kale.responseservice.dto.response.MultipleChoiceInfo;
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
public class GetShortStatisticsRes {
    String sureyTitle;
    String questionTitle;

    List<MultipleChoiceInfo> multipleChoiceInfos;

    List<String> subjectiveAnswers = new ArrayList<>();
}
