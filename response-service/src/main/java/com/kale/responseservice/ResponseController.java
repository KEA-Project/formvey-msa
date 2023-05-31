package com.kale.responseservice;

import com.kale.responseservice.client.MemberServiceClient;
import com.kale.responseservice.dto.client.GetMemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/response-service/responses")
public class ResponseController {
    private final MemberServiceClient memberServiceClient;

    @GetMapping("/{memberId}")
    private void test(@PathVariable Long memberId) {
        GetMemberRes getMemberRes = memberServiceClient.getMemberInfo(memberId).getResult();
        System.out.println(getMemberRes.getEmail());
        System.out.println(getMemberRes.getNickname());
        System.out.println(getMemberRes.getId());
        System.out.println(getMemberRes.getPoint());
    }

}

