package com.kale.responseservice.client;

import com.kale.responseservice.common.BaseResponse;
import com.kale.responseservice.dto.client.GetQuestionRes;
import com.kale.responseservice.dto.client.GetSurveyRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "survey-service", url = "http://localhost:8082/survey-service")
public interface SurveyServiceClient {

    // 설문 조회
    @RequestMapping(method = RequestMethod.GET, value = "/surveys/{surveyId}", consumes = "application/json")
    BaseResponse<GetSurveyRes> getSurveyById(@PathVariable Long surveyId);

    // 질문 조회
    @RequestMapping(method = RequestMethod.GET, value = "/surveys/{questionId}", consumes = "application/json")
    BaseResponse<GetQuestionRes> getQuestionById(@PathVariable Long questionId);

    // 설문 응답 수 증가
    @RequestMapping(method = RequestMethod.PATCH, value = "/surveys/increment-cnt/{surveyId}", consumes = "application/json")
    void incrementCount(@PathVariable Long surveyId);
}
