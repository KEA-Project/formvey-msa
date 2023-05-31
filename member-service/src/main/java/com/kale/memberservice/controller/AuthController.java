package com.kale.memberservice.controller;

import com.kale.memberservice.common.BaseResponse;
import com.kale.memberservice.dto.PostLoginReq;
import com.kale.memberservice.dto.PostLoginRes;
import com.kale.memberservice.service.AuthService;
import com.kale.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/member-service")
public class AuthController {
    private final AuthService authService;

    /**
     * 이메일 로그인 및 가입 여부 확인
     * [POST] /login/email
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/email")
    @Operation(summary = "이메일 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "2014", description = "비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "2015", description = "존재하지 않는 이메일입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    private BaseResponse<PostLoginRes> emailLogin(@RequestBody PostLoginReq dto) {
        PostLoginRes postLoginRes = authService.emailLogin(dto);

        return new BaseResponse<>(postLoginRes);
    }

    /**
     * 카카오 로그인 및 가입 여부 확인(미가입 시 회원가입 자동 진행 후 로그인도 자동 진행됨)
     * [POST] /login/kakao/{access-token}
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/kakao/{access-token}")
    @Operation(summary = "카카오 소셜 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    private BaseResponse<PostLoginRes> kakaoLogin(@PathVariable("access-token") String token) {
        PostLoginRes postLoginRes = authService.kakaoLogin(token);

        return new BaseResponse<>(postLoginRes);
    }

    /**
     * 로그아웃
     * [PATCH] /logout/{memberId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{memberId}")
    @Operation(summary = "로그아웃", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameter(name = "memberId", description = "로그아웃할 유저 인덱스", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다."),
            @ApiResponse(responseCode = "2010", description = "유저 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    private BaseResponse<String> logout(@PathVariable("memberId") Long memberId) {
//        //jwt에서 idx 추출.
//        Long memberIdByJwt = jwtService.getUserIdx();
//        //memberId와 접근한 유저가 같은지 확인
//        if (memberId != memberIdByJwt) {
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }
        authService.logout(memberId);
        String result = "회원 로그아웃을 완료했습니다.";

        return new BaseResponse<>(result);
    }
}
