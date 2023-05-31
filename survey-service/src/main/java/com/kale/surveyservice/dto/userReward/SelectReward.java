package com.kale.surveyservice.dto.userReward;


import com.kale.surveyservice.domain.UserReward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SelectReward {
    private Long memberId;
    private String rewardUrl;

    public static UserReward toEntity(Long memberId,String rewardUrl){
        return UserReward.builder()
                .memberId(memberId)
                .rewardImage(rewardUrl)
                .build();
    }
}
