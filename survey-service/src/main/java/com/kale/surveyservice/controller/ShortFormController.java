package com.kale.surveyservice.controller;

import com.kale.surveyservice.dto.client.GetClientShortFormRes;
import com.kale.surveyservice.common.BaseResponse;
import com.kale.surveyservice.dto.shortForm.*;
import com.kale.surveyservice.service.ShortFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/survey-service/shortForms")
public class ShortFormController {
    private final ShortFormService shortFormService;

    /**
     * 짧폼 조회 (client)
     */
    @ResponseBody
    @GetMapping("/{shortFormId}")
    private BaseResponse<GetClientShortFormRes> getShortFormById(@PathVariable Long shortFormId) {
        GetClientShortFormRes getClientShortFormRes = shortFormService.getClientShortFormRes(shortFormId);

        return new BaseResponse<>(getClientShortFormRes);
    }

    /**
     * 짧폼 응답 수 증가
     */
    @ResponseBody
    @PatchMapping("/increment-cnt/{shortFormId}")
    private void incrementShortCount(@PathVariable Long shortFormId) {
        shortFormService.incrementShortCount(shortFormId);

    }
    /**
     * 짧폼 리스트 조회
     * [GET] /shortForms/board/{memberId}
     * @return BaseResponse<List<GetShortFormListRes>>
     */
    @ResponseBody
    @GetMapping("/board/{memberId}")
    @Operation(summary = "짧폼 리스트 조회")
    public BaseResponse<List<GetShortFormListRes>> getShortFormList(@RequestParam("page") int page, @RequestParam("size") int size, @PathVariable Long memberId) {
        List<GetShortFormListRes> getShortFormListRes = shortFormService.getShortFormList(page, size, memberId);

        return new BaseResponse<>(getShortFormListRes);
    }

    /**
     * 짧폼 상세 조회
     * [GET] /shortForms/info/{shortFormId}
     * @return BaseResponse<GetShortFormRes>
     */
    @ResponseBody
    @GetMapping("/info/{shortFormId}")
    @Operation(summary = "짧폼 상세 조회")
    public BaseResponse<GetShortFormRes> getShortForm(@PathVariable Long shortFormId) {
        GetShortFormRes getShortFormRes = shortFormService.getShortForm(shortFormId);

        return new BaseResponse<>(getShortFormRes);
    }

    /**
     * 짧폼 메인 조회
     * [GET] /shortForms/random/{memberId}
     * @return BaseResponse<GetShortFormMainRes>
     */
    @ResponseBody
    @GetMapping("/random/{memberId}")
    @Operation(summary = "짧폼 랜덤 조회", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "memberId", description = "로그인한 유저의 인덱스", required = true)
    public BaseResponse<GetShortFormMainRes> getRandomShortForm(@PathVariable Long memberId) {
        GetShortFormMainRes getShortFormMainRes = shortFormService.getShortFormMain(memberId);

        return new BaseResponse<>(getShortFormMainRes);
    }
}
