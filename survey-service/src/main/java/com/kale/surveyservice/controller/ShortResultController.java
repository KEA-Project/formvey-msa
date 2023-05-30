package com.kale.surveyservice.controller;

import com.kale.surveyservice.common.BaseResponse;
import com.kale.surveyservice.dto.shortResult.GetShortResultBoardRes;
import com.kale.surveyservice.service.ShortResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
@RequestMapping("/shortresults")
public class ShortResultController {
    private final ShortResultService shortResultService;
    //private final JwtService jwtService;

    /**
     * 짧폼 해금
     * [POST] /shortresults/{shortFormId}/{memberId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{shortFormId}/{memberId}")
    @Operation(summary = "짧폼 해금", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameters({@Parameter(name="shortFormId", description="해금할 짧폼 인덱스", required = true),
            @Parameter(name="memberId", description="해금할 유저 인덱스", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode="2001", description="JWT를 입력해주세요."),
            @ApiResponse(responseCode="2002", description="유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode="2003", description="권한이 없는 유저의 접근입니다."),
            @ApiResponse(responseCode="2052", description="짧폼을 해금하기 위한 포인트가 부족합니다.")
    })
    private BaseResponse<String> responseShortResult(@PathVariable Long shortFormId, @PathVariable Long memberId) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//
//        //memberId와 접근한 유저가 같은지 확인
//        if (memberId != memberIdByJwt)
//            return new BaseResponse<>(INVALID_USER_JWT);

        shortResultService.responseShortResult(shortFormId, memberId);
        String result = "짧폼이 잠금 해제되었습니다.";

        return new BaseResponse<>(result);
    }

    /**
     * 해금된 짧폼 리스트 조회
     * [GET] /shortresults/board/{memberId}
     * @return BaseResponse<List<GetShortResultListRes>>
     */
    @ResponseBody
    @GetMapping("/board/{memberId}")
    @Operation(summary = "해금된 짧폼 리스트 조회", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name="memberId", description="해금한 유저 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode="2001", description="JWT를 입력해주세요."),
            @ApiResponse(responseCode="2002", description="유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode="2003", description="권한이 없는 유저의 접근입니다.")
    })
    public BaseResponse<List<GetShortResultBoardRes>> getShortResultBoard(@RequestParam("page") int page, @RequestParam("size") int size, @PathVariable Long memberId) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (memberId != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }

        List<GetShortResultBoardRes> result = shortResultService.getShortResultBoard(page, size, memberId);

        return new BaseResponse<>(result);
    }
}
