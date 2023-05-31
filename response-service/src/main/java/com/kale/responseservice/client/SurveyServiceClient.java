package com.kale.responseservice.client;

import com.kale.responseservice.common.BaseResponse;
import com.kale.responseservice.dto.client.GetSurveyRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "survey-service", url = "http://localhost:8082/survey-service")
public interface SurveyServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/surveys/{surveyId}", consumes = "application/json")
    BaseResponse<GetSurveyRes> getSurveyById(@PathVariable Long surveyId);
}
