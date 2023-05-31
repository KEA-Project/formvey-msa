package com.kale.surveyservice.service;


import com.kale.surveyservice.client.MemberServiceFeignClient;
import com.kale.surveyservice.client.ResponseServiceFeignClient;
import com.kale.surveyservice.domain.UserReward;
import com.kale.surveyservice.dto.response.GetResponseListInfoRes;
import com.kale.surveyservice.dto.userReward.GetUserRewardListRes;
import com.kale.surveyservice.dto.userReward.PostRandomRewardReq;
import com.kale.surveyservice.dto.userReward.PostSelectRewardReq;
import com.kale.surveyservice.dto.userReward.SelectReward;
import com.kale.surveyservice.repository.UserRewardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRewardService {
    private final UserRewardRepository userRewardRepository;

    @Autowired
    private ResponseServiceFeignClient responseServiceFeignClient;

    /**
     * 랜덤 발송
     */
    public void randomReward(PostRandomRewardReq dto, Long surveyId) {
        List<GetResponseListInfoRes> responses=responseServiceFeignClient.getResListInfo(surveyId);

        //리워드 랜덤 매핑
        for(int i=0;i< dto.getRewardUrl().size();i++){
            int index=(int) (Math.random()*responses.size());
            Long memberId=responses.get(index).getMemberId();
            UserReward userReward= SelectReward.toEntity(memberId,dto.getRewardUrl().get(i));
            userRewardRepository.save(userReward);
        }
    }

    /**
     * 지정 발송
     */
    public void selectReward(PostSelectRewardReq dto) {
        //리워드 지정 발송
        for(int i=0; i<dto.getRewards().size();i++){
            Long memberId=dto.getRewards().get(i).getMemberId();
            UserReward userReward= SelectReward.toEntity(memberId,dto.getRewards().get(i).getRewardUrl());
            userRewardRepository.save(userReward);
        }
    }

    /**
     * 리워드 조회
     */
    public List<GetUserRewardListRes> myReward(Long memberId) {
        List<GetUserRewardListRes> getUserRewardListRes=new ArrayList<>();
        List<UserReward> userRewards=userRewardRepository.findByMemberId(memberId);

        for(UserReward userReward : userRewards){
            getUserRewardListRes.add(new GetUserRewardListRes(userReward.getId(), userReward.getRewardImage()));
        }
        return getUserRewardListRes;
    }

}
