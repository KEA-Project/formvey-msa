package com.kale.surveyservice.dto.userReward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetUserRewardListRes {
    private Long userRewardId;
    private String rewardUrl;

}
