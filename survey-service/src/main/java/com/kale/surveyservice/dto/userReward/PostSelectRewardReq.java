package com.kale.surveyservice.dto.userReward;

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
public class PostSelectRewardReq {
    private List<SelectReward> rewards=new ArrayList<>();

}
