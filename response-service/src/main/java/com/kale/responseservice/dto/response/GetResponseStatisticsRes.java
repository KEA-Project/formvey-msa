package com.kale.responseservice.dto.response;

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
public class GetResponseStatisticsRes {
    Long questionId;

    int questionIdx;

    String questionTitle;

    List<MultipleChoiceInfo> multipleChoiceInfos; // 객관식 옵션에 대한 정보 리스트

    List<String> subjectiveAnswers = new ArrayList<>();
}


