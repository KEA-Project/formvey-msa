package com.kale.surveyservice.dto.shortResult;


import com.kale.surveyservice.domain.ShortForm;
import com.kale.surveyservice.domain.ShortResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
public class PostShortResultReq {

    public static ShortResult toEntity(Long memberId, ShortForm shortForm){
        return ShortResult.builder()
                .memberId(memberId)
                .shortForm(shortForm)
                .build();
    }

}
