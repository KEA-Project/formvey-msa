package com.kale.responseservice.client;

import com.kale.responseservice.common.BaseResponse;
import com.kale.responseservice.dto.client.GetMemberRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "member-service", url = "http://localhost:8081/member-service")
public interface MemberServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/members/info/{memberId}", consumes = "application/json")
    BaseResponse<GetMemberRes> getMemberInfo(@PathVariable Long memberId);
}
