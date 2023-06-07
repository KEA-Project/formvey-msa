package com.kale.responseservice.client;

import com.kale.responseservice.common.BaseResponse;
import com.kale.responseservice.dto.client.GetMemberRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service", url = "http://localhost:8081/member-service")
public interface MemberServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/members/res/info/{memberId}", consumes = "application/json")
    BaseResponse<GetMemberRes> getMemberInfo(@PathVariable Long memberId);

    // 멤버 포인트 증가
    @RequestMapping(method = RequestMethod.GET, value = "/members/increment-point/{memberId}", consumes = "application/json")
    void incrementPoint(@PathVariable Long memberId, @RequestParam("point") int point);
}
