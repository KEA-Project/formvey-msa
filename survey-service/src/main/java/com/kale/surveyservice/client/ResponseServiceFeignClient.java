package com.kale.surveyservice.client;

import com.kale.surveyservice.dto.response.GetResponseListInfoRes;
import com.kale.surveyservice.dto.response.GetResponseListRes;
import com.kale.surveyservice.dto.response.GetShortResponseListRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("response-service")
public interface ResponseServiceFeignClient {

    //response 서비스에서 해당하는 로직 수행
    @DeleteMapping("/responses/delete/{surveyId}")
    void deleteResponses(@PathVariable("surveyId") Long surveyId);

    @GetMapping("/responses/list/count/{memberId}")
    List<GetResponseListRes> getResCount(@PathVariable("memberId") Long memberId);

    @GetMapping("/responses/list/info/{surveyId}")
    List<GetResponseListInfoRes> getResListInfo(@PathVariable("surveyId") Long surveyId);

    @GetMapping("/shortanswers/list/count/{memberId}")
    List<GetShortResponseListRes> getShortResCount(@PathVariable("memberId") Long memberId);

}
