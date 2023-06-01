package com.kale.responseservice.controller.response;

import com.kale.responseservice.client.MemberServiceClient;
import com.kale.responseservice.client.SurveyServiceClient;
import com.kale.responseservice.common.BaseResponse;
import com.kale.responseservice.dto.response.*;
import com.kale.responseservice.service.response.ResponseService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/response-service/responses")
public class ResponseController {
    private final ResponseService responseService;
//    private final JwtService jwtService;

    /**
     * 설문 응답
     * [POST] /responses/{surveyId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{surveyId}/{memberId}")
    @Operation(summary = "설문 응답", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "surveyId", description = "응답할 설문 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다."),
            @ApiResponse(responseCode = "2040", description = "본인이 생성한 설문입니다."),
            @ApiResponse(responseCode = "2041", description = "이미 응답한 설문입니다.")

    })
    private BaseResponse<String> responseSurvey(@RequestBody PostResponseReq dto, @PathVariable Long surveyId) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (dto.getMemberId() != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }
        responseService.responseSurvey(dto, surveyId);
        String result = "응답이 등록되었습니다";

        return new BaseResponse<>(result);
    }

    /**
     * 설문 응답 수정
     * [PUT] /responses/update/{surveyId}/{responseId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PutMapping("/update/{surveyId}/{responseId}")
    @Operation(summary = "설문 응답 수정", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameters({@Parameter(name = "surveyId", description = "수정할 설문 인덱스", required = true),
            @Parameter(name = "responseId", description = "수정할 응답 인덱스", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다.")
    })
    private BaseResponse<String> updateResponse(@RequestBody PostResponseReq dto, @PathVariable Long surveyId, @PathVariable Long responseId) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (dto.getMemberId() != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }

        responseService.updateResponse(dto,surveyId,responseId);
        String result = "응답이 수정되었습니다";

        return new BaseResponse<>(result);
    }

    /**
     * 응답 삭제
     * [PATCH] /responses/delete/{responseId}
     * @return BaseResponse<String>
     */
    @PatchMapping("/delete/{responseId}")
    @Operation(summary = "응답 삭제", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "responseId", description = "삭제할 응답 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다.")
    })
    public BaseResponse<String> deleteResponse(@PathVariable Long responseId, @RequestBody DeleteResponseReq deleteResponseReq) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (deleteResponseReq.getMemberId() != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }
        responseService.deleteResponse(responseId);
        String result = "응답이 삭제되었습니다.";

        return new BaseResponse<>(result);
    }


    /**
     * 응답 설문 리스트 조회
     * [GET] /responses/list/{memberId}
     * @return BaseResponse<List<GetResponseListRes>>
     */
    @ResponseBody
    @GetMapping("/list/{memberId}")
    @Operation(summary = "응답 설문 리스트 조회", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "memberId", description = "응답한 유저의 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다.")
    })
    public BaseResponse<GetResponseList> getResponseList(@PathVariable Long memberId, @RequestParam("page") int page, @RequestParam("size") int size) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (memberId != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }
        GetResponseList getResponseList = responseService.getResponseList(memberId,page,size);

        return new BaseResponse<>(getResponseList);
    }

    /**
     * 응답 내용 조회
     * [GET] /responses/info/{responseId}
     * @return BaseResponse<GetResponseInfoRes>
     */
    @ResponseBody
    @GetMapping("/info/{responseId}")
    @Operation(summary = "응답 내용 조회")
    @Parameter(name = "responseId", description = "조회할 응답 인덱스", required = true)
    public BaseResponse<GetResponseInfoRes> getResponseInfo(@PathVariable Long responseId) {
        GetResponseInfoRes getResponseInfoRes = responseService.getResponseInfo(responseId);

        return new BaseResponse<>(getResponseInfoRes);
    }
    /**
     * 제작 설문 개별 응답 조회
     * [GET] /responses/Individual/{surveyId}
     * @return BaseResponse<List<GetResponseIndividualRes>>
     */
    @ResponseBody
    @GetMapping("/Individual/{surveyId}")
    @Operation(summary = "제작 설문 개별 응답 조회")
    @Parameter(name = "surveyId", description = "제작한 설문 인덱스", required = true)
    public BaseResponse<List<GetResponseIndividualRes>> getResponseIndividual(@PathVariable Long surveyId, @RequestParam("page") int page, @RequestParam("size") int size) {
        List<GetResponseIndividualRes> getResponseIndividualRes = responseService.getResponseIndividual(surveyId, page, size);

        return new BaseResponse<>(getResponseIndividualRes);
    }

    /**
     * 제작 설문 응답 통계 조회
     * [GET] /responses/statistics/{surveyId}
     * @return BaseResponse<List<GetResponseStatisticsRes>>
     */
    @ResponseBody
    @GetMapping("/statistics/{surveyId}")
    @Operation(summary = "제작 설문 응답 통계 조회")
    @Parameter(name = "surveyId", description = "제작한 설문 인덱스", required = true)
    public BaseResponse<List<GetResponseStatisticsRes>> getResponseStatistics(@PathVariable Long surveyId) {
        List<GetResponseStatisticsRes> getResponseStatisticsRes = responseService.getResponseStatistics(surveyId);

        return new BaseResponse<>(getResponseStatisticsRes);
    }
}

