package com.kale.surveyservice.dto.shortOption;

import com.kale.surveyservice.domain.ShortForm;
import com.kale.surveyservice.domain.ShortOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostShortOptionReq {

    private int shortIndex;

    private String shortContent;

    //-------------------------------------------------------------

    public static ShortOption toEntity(ShortForm shortForm, PostShortOptionReq dto){
        return ShortOption.builder()
                .shortForm(shortForm)
                .shortIndex(dto.shortIndex)
                .shortContent(dto.shortContent)
                .build();
    }
}
