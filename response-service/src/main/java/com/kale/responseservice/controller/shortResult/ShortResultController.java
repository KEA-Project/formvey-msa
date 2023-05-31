package com.kale.responseservice.controller.shortResult;

import com.kale.formvey.config.BaseResponse;
import com.kale.formvey.dto.shortResult.GetShortResultBoardRes;
import com.kale.formvey.service.shortResult.ShortResultService;
import com.kale.formvey.utils.JwtService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kale.formvey.config.BaseResponseStatus.INVALID_USER_JWT;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/shortresults")
public class ShortResultController {
    private final ShortResultService shortResultService;
    private final JwtService jwtService;

    /**
     * 짧폼 해금
     * [POST] /shortresults/{shortFormId}/{memberId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{shortFormId}/{memberId}")
    @ApiOperation(value = "짧폼 해금", notes = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @ApiImplicitParams({@ApiImplicitParam(name="shortFormId", value="해금할 짧폼 인덱스", required = true),
            @ApiImplicitParam(name="memberId", value="해금할 유저 인덱스", required = true)})
    @ApiResponses({
            @ApiResponse(code=2001, message="JWT를 입력해주세요."),
            @ApiResponse(code=2002, message="유효하지 않은 JWT입니다."),
            @ApiResponse(code=2003, message="권한이 없는 유저의 접근입니다."),
            @ApiResponse(code=2052, message="짧폼을 해금하기 위한 포인트가 부족합니다.")
    })
    private BaseResponse<String> responseShortResult(@PathVariable Long shortFormId, @PathVariable Long memberId) {
        //jwt에서 idx 추출.
        Long memberIdByJwt = jwtService.getUserIdx();

        //memberId와 접근한 유저가 같은지 확인
        if (memberId != memberIdByJwt)
            return new BaseResponse<>(INVALID_USER_JWT);

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
    @ApiOperation(value = "해금된 짧폼 리스트 조회", notes = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @ApiImplicitParams({@ApiImplicitParam(name="memberId", value="해금한 유저 인덱스", required = true)})
    @ApiResponses({
            @ApiResponse(code=2001, message="JWT를 입력해주세요."),
            @ApiResponse(code=2002, message="유효하지 않은 JWT입니다."),
            @ApiResponse(code=2003, message="권한이 없는 유저의 접근입니다.")
    })
    public BaseResponse<List<GetShortResultBoardRes>> getShortResultBoard(@RequestParam("page") int page, @RequestParam("size") int size, @PathVariable Long memberId) {
        //jwt에서 idx 추출.
        Long memberIdByJwt = jwtService.getUserIdx();
        //memberId와 접근한 유저가 같은지 확인
        if (memberId != memberIdByJwt) {
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        List<GetShortResultBoardRes> result = shortResultService.getShortResultBoard(page, size, memberId);

        return new BaseResponse<>(result);
    }
}
