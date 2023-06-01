package com.kale.responseservice.dto.response;

import com.kale.responseservice.domain.Response;
import com.kale.responseservice.dto.answer.PostAnswerReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponseReq {

    private Long memberId;

    private List<PostAnswerReq> answers = new ArrayList<>();
    private LocalDateTime responseDate;

    public static Response toEntity(Long memberId, Long surveyId, PostResponseReq dto){
        return Response.builder()
                .memberId(memberId)
                .surveyId(surveyId)
                .responseDate(dto.responseDate)
                .build();
    }
}
