package com.kale.responseservice.controller.shortAnswer;

import com.kale.responseservice.common.BaseResponse;
import com.kale.responseservice.dto.shortAnswer.PostShortAnswerReq;
import com.kale.responseservice.service.shortAnswer.ShortAnswerService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/response-service/shortanswers")
public class ShortAnswerController {
    private final ShortAnswerService shortAnswerService;
//    private final JwtService jwtService;

    /**
     * 짧폼 답변
     * [POST] /shortanswers/{shortFormId}/{memberId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{shortFormId}/{memberId}")
    @Operation(summary = "짧폼 답변", description = "헤더에 jwt 필요(key: X-ACCESS-TOKEN, value: jwt 값)")
    @Parameters({@Parameter(name="shortFormId", description = "응답한 짧폼 인덱스", required = true),
            @Parameter(name="memberId", description = "응답한 유저 인덱스", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "2001", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2002", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2003", description = "권한이 없는 유저의 접근입니다.")
    })
    private BaseResponse<String> responseShortAnswer(@RequestBody PostShortAnswerReq dto,
                                                     @PathVariable Long shortFormId, @PathVariable Long memberId) {
//            //jwt에서 idx 추출.
//            Long memberIdByJwt = jwtService.getUserIdx();
//            //memberId와 접근한 유저가 같은지 확인
//            if(memberId!= memberIdByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            shortAnswerService.responseShortAnswer(dto, shortFormId, memberId);
            String result = "응답이 등록되었습니다";

            return new BaseResponse<>(result);

    }

}
