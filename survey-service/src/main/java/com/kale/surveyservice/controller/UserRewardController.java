package com.kale.surveyservice.controller;

import com.kale.surveyservice.common.BaseResponse;
import com.kale.surveyservice.dto.userReward.GetUserRewardListRes;
import com.kale.surveyservice.dto.userReward.PostRandomRewardReq;
import com.kale.surveyservice.dto.userReward.PostSelectRewardReq;
import com.kale.surveyservice.service.UserRewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/survey-service/rewards")
public class UserRewardController {
    private final UserRewardService userRewardService;
    //private final JwtService jwtService;

    /**
     * 랜덤 발송
     * [POST] /rewards/random/{surveyId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/random/{surveyId}")
    @Operation(summary = "랜덤 발송", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "surveyId", description = "제작한 설문 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다.")
    })
    private BaseResponse<String> randomReward(@RequestBody PostRandomRewardReq dto, @PathVariable Long surveyId) {
        //jwt에서 idx 추출.
        //Long memberIdByJwt = jwtService.getUserIdx();
        userRewardService.randomReward(dto, surveyId);

        String result = "리워드가 전송되었습니다.";
        return new BaseResponse<>(result);
    }

    /**
     * 지정 발송
     * [POST] /rewards/select
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/select")
    @Operation(summary = "지정 발송", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다.")
    })
    private BaseResponse<String> selectReward(@RequestBody PostSelectRewardReq dto) {
        //jwt에서 idx 추출.
        //Long memberIdByJwt = jwtService.getUserIdx();
        userRewardService.selectReward(dto);

        String result = "리워드가 전송되었습니다.";
        return new BaseResponse<>(result);
    }

    /**
     * 리워드 조회
     * [GET] /rewards/{memberId}
     * @return BaseResponse<List<GetUserRewardListRes>>
     */
    @ResponseBody
    @GetMapping("/{memberId}")
    @Operation(summary = "리워드 보관함", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "memberId", description = "멤버 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description= "권한이 없는 유저의 접근입니다.")
    })
    private BaseResponse<List<GetUserRewardListRes>> myReward(@PathVariable Long memberId) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (memberId != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }
        List<GetUserRewardListRes> getUserRewardListRes=userRewardService.myReward(memberId);

        return new BaseResponse<>(getUserRewardListRes);
    }


}

