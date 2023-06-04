package com.kale.surveyservice.common;

import com.kale.responseservice.common.BaseException;
import com.kale.responseservice.common.BaseResponse;
import com.kale.responseservice.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(com.kale.responseservice.common.BaseException.class)
    public com.kale.responseservice.common.BaseResponse<BaseResponseStatus> baseException(BaseException e) {
        System.out.println("Handle CommonException:" + e.getMessage());
        return new BaseResponse<>(e.getStatus());
    }
}
