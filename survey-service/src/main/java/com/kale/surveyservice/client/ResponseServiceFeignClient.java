package com.kale.surveyservice.client;

import com.kale.surveyservice.common.BaseResponse;
import com.kale.surveyservice.dto.response.GetResponseCountRes;
import com.kale.surveyservice.dto.response.GetResponseListInfoRes;
import com.kale.surveyservice.dto.shortForm.GetShortResponseCountRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="response-service", url = "http://172.16.212.108:8083/response-service")
public interface ResponseServiceFeignClient {

    //response 서비스에서 해당하는 로직 수행
    @RequestMapping(method= RequestMethod.DELETE, value = "/responses/delete/{surveyId}", consumes="application/json")
    BaseResponse<String> deleteResponses(@PathVariable("surveyId") Long surveyId);

    @RequestMapping(method = RequestMethod.GET, value = "/responses/list/count/{memberId}", consumes = "application/json")
    BaseResponse<List<GetResponseCountRes>> getResCount(@PathVariable("memberId") Long memberId);

    @RequestMapping(method = RequestMethod.GET, value = "/responses/list/info/{surveyId}", consumes = "application/json")
    BaseResponse<List<GetResponseListInfoRes>> getResListInfo(@PathVariable("surveyId") Long surveyId);

    @RequestMapping(method = RequestMethod.GET, value = "/shortanswers/list/count/{memberId}", consumes = "application/json")
    BaseResponse<List<GetShortResponseCountRes>> getShortResCount(@PathVariable("memberId") Long memberId);

    @RequestMapping(method = RequestMethod.GET, value = "/shortanswers/exist/{memberId}/{shortformId}", consumes = "application/json")
    BaseResponse<String> existShortResponse(@PathVariable("memberId") Long memberId, @PathVariable("shortformId") Long shortformId);
}
