package com.kale.surveyservice.client;

import com.kale.surveyservice.common.BaseResponse;
import com.kale.surveyservice.dto.member.GetMemberInfoSubRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "member-service", url = "http://172.16.212.106:8081/member-service")
public interface MemberServiceFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/members/info/sub/{memberId}", consumes = "application/json")
    BaseResponse<GetMemberInfoSubRes> getInfoSub(@PathVariable("memberId") Long memberId);

    @RequestMapping(method = RequestMethod.GET, value = "/members/point/{memberId}", consumes = "application/json")
    BaseResponse<String> modifyPoint(@PathVariable("memberId") Long memberId);
}
