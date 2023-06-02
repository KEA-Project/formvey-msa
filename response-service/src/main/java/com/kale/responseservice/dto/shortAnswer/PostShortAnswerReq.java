package com.kale.responseservice.dto.shortAnswer;

import com.kale.responseservice.domain.ShortAnswer;
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
public class PostShortAnswerReq {
    private List<String> shortAnswer = new ArrayList<>();

    private int point;

    public static ShortAnswer toEntity(Long memberId, Long shortFormId, PostShortAnswerReq dto){
        return ShortAnswer.builder()
                .memberId(memberId)
                .shortFormId(shortFormId)
                .shortAnswer(dto.shortAnswer.toString())
                .build();
    }
}
