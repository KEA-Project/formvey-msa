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
public class PostRandomRewardReq {
    private List<String> rewardUrl=new ArrayList<>();
}
