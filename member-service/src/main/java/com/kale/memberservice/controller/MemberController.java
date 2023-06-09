package com.kale.memberservice.controller;

import com.kale.memberservice.common.BaseResponse;
import com.kale.memberservice.dto.*;
import com.kale.memberservice.service.AuthService;
import com.kale.memberservice.service.JwtService;
import com.kale.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import static com.kale.memberservice.common.BaseResponseStatus.INVALID_USER_JWT;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member-service/members")
public class MemberController {
    private final Environment env;
    private final MemberService memberService;
    private final JwtService jwtService;

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("check is called in Member Service");
        log.info("Server Port from HttpServletRequest: port = {}", request.getServerPort());
        log.info("Server Port from Environment: port = {}", env.getProperty("local.server.port"));

        return String.format("check is called in Member Service, Server port is %s from HttpServletRequest and %s from Environment", request.getServerPort(), env.getProperty("local.server.port"));
    }

    /**
     * 멤버 포인트 증가
     */
    @ResponseBody
    @GetMapping("/increment-point/{memberId}")
    @Operation(summary = "멤버 포인트 증가(응답 서비스에서 요청)")
    private void incrementPoint(@PathVariable Long memberId, @RequestParam("point") int point) {
        memberService.incrementPoint(memberId, point);
    }

    /**
     * 이메일 회원가입
     * [POST] /members/signup
     * @return BaseResponse<PostMemberRes>
     */
    @ResponseBody
    @PostMapping("/signup")
    @Operation(summary = "이메일 회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "2016", description = "이메일 형식을 확인해주세요."),
            @ApiResponse(responseCode = "2017", description = "중복된 이메일 입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    private BaseResponse<PostMemberRes> emailSignup(@RequestBody PostMemberReq dto) {
        PostMemberRes postMemberRes = memberService.emailSignup(dto);

        return new BaseResponse<>(postMemberRes);
    }

    /**
     * 회원 정보 조회 (client 요청)
     * [GET] /members/res/info/{memberId}
     * @return BaseResponse<GetMemberRes>
     */
    @ResponseBody
    @GetMapping("/res/info/{memberId}")
    @Operation(summary = "회원 정보 조회 (응답 서비스에서 요청)")
    private BaseResponse<GetMemberRes> getResMemberInfo(@PathVariable Long memberId) {
        GetMemberRes getMemberRes = memberService.getMemberInfo(memberId);

        return new BaseResponse<>(getMemberRes);
    }

    /**
     * 회원 정보 조회
     * [GET] /members/info/{memberId}
     * @return BaseResponse<GetMemberRes>
     */
    @ResponseBody
    @GetMapping("/info/{memberId}")
    @Operation(summary = "회원 정보 조회", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "memberId",  description = "조회할 유저 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다."),
            @ApiResponse(responseCode = "2010", description = "유저 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    private BaseResponse<GetMemberRes> getMemberInfo(@PathVariable Long memberId) {
        //jwt에서 idx 추출.
        Long memberIdByJwt = jwtService.getUserIdx();
        //memberId와 접근한 유저가 같은지 확인
        if (memberId != memberIdByJwt) {
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        GetMemberRes getMemberRes = memberService.getMemberInfo(memberId);

        return new BaseResponse<>(getMemberRes);
    }

    /**
     * 회원 정보 수정
     * [PATCH] /members/edit/{memberId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/edit/{memberId}")
    @Operation(summary = "회원 정보 수정", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "memberId", description = "수정할 유저 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다."),
            @ApiResponse(responseCode = "2010", description = "유저 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    private BaseResponse<String> editProfile(@RequestBody PatchMemberReq dto, @PathVariable Long memberId) {
        //jwt에서 idx 추출.
        Long memberIdByJwt = jwtService.getUserIdx();
        //memberId와 접근한 유저가 같은지 확인
        if (memberId != memberIdByJwt)
            return new BaseResponse<>(INVALID_USER_JWT);

        memberService.editProfile(memberId, dto);
        String result = "회원 정보 수정이 완료되었습니다";

        return new BaseResponse<>(result);
    }

    /**
     * 설문 서비스에서 요청하는 회원 정보 api
     * [GET] /members/info/sub/{memberId}
     * @return BaseResponse<GetMemberInfoSubRes>
     */
    @ResponseBody
    @GetMapping("/info/sub/{memberId}")
    @Operation(summary = "설문 서비스에서 요청하는 회원 정보 api(설문 서비스에서 요청)")
    @Parameter(name = "memberId",  description = "조회할 유저 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2010", description = "유저 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    private BaseResponse<GetMemberInfoSubRes> getInfoSub(@PathVariable("memberId") Long memberId) {

        GetMemberInfoSubRes getMemberInfoSubRes=memberService.getMemberSubInfo(memberId);
        return new BaseResponse<>(getMemberInfoSubRes);
    }

    /**
     * 설문 서비스에서 짧폼 해금 시 보내는 포인트 차감 api
     * [GET] /members/point/{memberId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/point/{memberId}")
    @Operation(summary = "설문 서비스에서 짧폼 해금 시 보내는 포인트 차감 api(설문 서비스에서 요청)")
    @Parameter(name = "memberId",  description = "포인트 차감할 유저 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2010", description = "유저 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    private BaseResponse<String> modifyPoint(@PathVariable("memberId") Long memberId) {

        memberService.modifyPoint(memberId);
        String result="포인트 차감되었습니다.";
        return new BaseResponse<>(result);
    }
}
