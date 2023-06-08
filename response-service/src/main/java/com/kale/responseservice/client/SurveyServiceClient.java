package com.kale.responseservice.client;

import com.kale.responseservice.common.BaseResponse;
import com.kale.responseservice.dto.client.GetOptionRes;
import com.kale.responseservice.dto.client.GetQuestionRes;
import com.kale.responseservice.dto.client.GetClientShortFormRes;
import com.kale.responseservice.dto.client.GetSurveyRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "survey-service", url = "http://172.16.212.107:8082/survey-service")
public interface SurveyServiceClient {

    // 설문 조회
    @RequestMapping(method = RequestMethod.GET, value = "/surveys/{surveyId}", consumes = "application/json")
    BaseResponse<GetSurveyRes> getSurveyById(@PathVariable Long surveyId);

    // 질문 리스트 조회
    @RequestMapping(method = RequestMethod.GET, value = "/surveys/questions/{surveyId}", consumes = "application/json")
    BaseResponse<List<GetQuestionRes>> getQuestionBySurveyId(@PathVariable Long surveyId);

    // 설문 응답 수 증가
    @RequestMapping(method = RequestMethod.GET, value = "/surveys/increment-cnt/{surveyId}", consumes = "application/json")
    void incrementCount(@PathVariable Long surveyId);

    // 짧폼 조회
    @RequestMapping(method = RequestMethod.GET, value = "/shortForms/{shortFormId}", consumes = "application/json")
    BaseResponse<GetClientShortFormRes> getShortFormById(@PathVariable Long shortFormId);

    // 짧폼 응답 수 증가
    @RequestMapping(method = RequestMethod.GET, value = "/shortForms/increment-cnt/{shortFormId}", consumes = "application/json")
    void incrementShortCount(@PathVariable Long shortFormId);

    // 짧폼아이디로 설문 조회
    @RequestMapping(method = RequestMethod.GET, value = "/surveys/{shortFormId}", consumes = "application/json")
    BaseResponse<GetSurveyRes> getSurveyByShortFormId(@PathVariable Long shortFormId);

    //짧폼아이디로 짧폼 보기 조회
    @RequestMapping(method = RequestMethod.GET, value = "/shortForms/shortOptions/{shortFormId}", consumes = "application/json")
    BaseResponse<List<GetOptionRes>> getShortOptionByShortFormId(@PathVariable Long shortFormId);
}