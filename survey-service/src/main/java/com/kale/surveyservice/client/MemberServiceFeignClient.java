package com.kale.surveyservice.client;

import com.kale.surveyservice.dto.member.GetMemberInfoSubRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("member-service")
public interface MemberServiceFeignClient {

    @GetMapping("/members/info/sub/{memberId}")
    GetMemberInfoSubRes getInfoSub(@PathVariable("memberId") Long memberId);

    @PatchMapping("/members/point/{memberId}")
    void modifyPoint(@PathVariable("memberId") Long memberId);
}
