package com.kale.responseservice.dto.answer;

import com.kale.responseservice.domain.Answer;
import com.kale.responseservice.domain.Response;
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
public class PostAnswerReq {

    private Long questionId;

    private List<String> content = new ArrayList<>();

    public static Answer toEntity(Long questionId, Response response, PostAnswerReq dto){
        return Answer.builder()
                .questionId(questionId)
                .response(response)
                .answerContent(dto.content.toString())
                .build();
    }
}
