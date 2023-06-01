package com.kale.surveyservice.controller;

import com.kale.surveyservice.client.MemberServiceFeignClient;
import com.kale.surveyservice.client.ResponseServiceFeignClient;
import com.kale.surveyservice.common.BaseResponse;
import com.kale.surveyservice.dto.survey.*;
import com.kale.surveyservice.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/survey-service/survey")
public class SurveyController {
    private final SurveyService surveyService;
    private final ResponseServiceFeignClient responseServiceFeignClient;

    //private final JwtService jwtService;

    /**
     * 첫 설문 생성(배포 / 임시) - status = 1 -> 짧폼등록 x(임시저장 ) / status = 2 -> 짧폼등록 o
     * [POST] /surveys/create
     * @return BaseResponse<PostSurveyRes>
     */
    @ResponseBody
    @PostMapping("/create/{status}")
    @Operation(summary = "설문 생성", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "status", description = "설문 상태", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2031", description = "설문 제목을 입력해주세요."),
            @ApiResponse(responseCode = "2032", description = "질문을 한 개 이상 등록해주세요."),
            @ApiResponse(responseCode = "2033", description = "설문 시작 날짜를 등록해주세요."),
            @ApiResponse(responseCode = "2034", description = "설문 종료 날짜를 등록해주세요.")
    })
    private BaseResponse<PostSurveyRes> createSurvey(@RequestBody PostSurveyReq dto, @PathVariable int status) {
        //Long memberId = jwtService.getUserIdx();
        PostSurveyRes postSurveyRes = surveyService.createSurvey(dto, status);

        return new BaseResponse<>(postSurveyRes);
    }

    /**
     * 존재하는 설문 업데이트(배포 / 임시) - status = 1 -> 짧폼등록 x(임시저장)  / status = 2 -> 짧폼등록 o
     * [PUT] /surveys/update/{surveyId}
     * @return BaseResponse<PostSurveyRes>
     */
    @ResponseBody
    @PutMapping("/update/{surveyId}/{status}")
    @Operation(summary = "설문 업데이트", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameters({
            @Parameter(name = "surveyId", description = "수정할 설문 인덱스", required = true),
            @Parameter(name = "status", description = "설문 상태", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2031", description = "설문 제목을 입력해주세요."),
            @ApiResponse(responseCode = "2032", description = "질문을 한 개 이상 등록해주세요."),
            @ApiResponse(responseCode = "2033", description = "설문 시작 날짜를 등록해주세요."),
            @ApiResponse(responseCode = "2034", description = "설문 종료 날짜를 등록해주세요.")
    })
    private BaseResponse<PostSurveyRes> updateSurvey(@RequestBody PostSurveyReq dto, @PathVariable Long surveyId, @PathVariable int status) {
        // memberId = jwtService.getUserIdx();
        PostSurveyRes postSurveyRes = surveyService.updateSurvey(surveyId,dto, status);

        return new BaseResponse<>(postSurveyRes);
    }

    /**
     * 설문 삭제
     * [DELETE] /surveys/delete/{surveyId}
     * 응답 삭제 api 요청해야함 - 추후에 kafka로 변경가능
     * @return BaseResponse<String>
     */
    @PatchMapping("/delete/{surveyId}")
    @Operation(summary = "설문 삭제", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "surveyId", description = "삭제할 설문 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다.")
    })
    public BaseResponse<String> deleteSurvey(@PathVariable Long surveyId, @RequestBody DeleteSurveyReq deleteSurveyReq) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (deleteSurveyReq.getMemberId() != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }
        String result = surveyService.deleteSurvey(surveyId);

        return new BaseResponse<>(result);
    }

    /**
     * 게시판 리스트 조회
     * [GET] /surveys/board
     * @return BaseResponse<List <GetSurveyBoardRes>>
     */
    @ResponseBody
    @GetMapping("/board")
    @Operation(summary = "게시판 리스트 조회")
    public BaseResponse<List<GetSurveyBoardRes>> getSurveyBoard(@RequestParam("page") int page, @RequestParam("size") int size) {
        List<GetSurveyBoardRes> getSurveyBoardRes = surveyService.getSurveyBoard(page, size);

        return new BaseResponse<>(getSurveyBoardRes);
    }

    /**
     * 제작 설문 리스트 조회
     * [GET] /surveys/list/{memberId}
     * @return BaseResponse<List < GetSurveyListRes>>
     */
    @ResponseBody
    @GetMapping("/list/{memberId}")
    @Operation(summary = "제작 설문 리스트 조회", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "memberId", description = "설문 제작 유저의 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다.")
    })
    public BaseResponse<GetSurveyList> getSurveyList(@PathVariable Long memberId, @RequestParam("page") int page, @RequestParam("size") int size) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (memberId != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }
        GetSurveyList getSurveyList = surveyService.getSurveyList(memberId,page,size);

        return new BaseResponse<>(getSurveyList);
    }

    /**
     * 설문 내용 조회
     * [GET] /surveys/info/{surveyId}
     * @return BaseResponse<GetSurveyInfoRes>
     */
    @ResponseBody
    @GetMapping("/info/{surveyId}")
    @Operation(summary = "설문 내용 조회", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "surveyId", description = "조회할 설문 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다."),
            @ApiResponse(responseCode = "2030", description = "설문 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    public BaseResponse<GetSurveyInfoRes> getSurveyInfo(@PathVariable Long surveyId) {
        //jwt에서 idx 추출.
        //Long memberIdByJwt = jwtService.getUserIdx();

        GetSurveyInfoRes getSurveyInfoRes = surveyService.getSurveyInfo(surveyId);

        return new BaseResponse<>(getSurveyInfoRes);
    }

    /**
     * 도넛 설문 차트 조회
     * [GET] /surveys/chart/{memberId}
     * @return BaseResponse<GetSurveyChartRes>
     */
    @GetMapping("/chart/{memberId}")
    @Operation(summary = "도넛 차트 조회", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "memberId", description = "조회 유저의 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다.")
    })
    private BaseResponse<GetSurveyChartRes> getSurveyChart(@PathVariable Long memberId) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (memberId != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }
        GetSurveyChartRes getSurveyChartRes = surveyService.getSurveyChart(memberId);

        return new BaseResponse<>(getSurveyChartRes);
    }
}
