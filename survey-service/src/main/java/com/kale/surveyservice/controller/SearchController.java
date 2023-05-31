package com.kale.surveyservice.controller;

import com.kale.surveyservice.common.BaseResponse;
import com.kale.surveyservice.dto.shortForm.GetShortFormListRes;
import com.kale.surveyservice.dto.survey.GetSurveyBoardRes;
import com.kale.surveyservice.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/survey-service/searchs")
public class SearchController {
    private final SearchService searchService;

    /**
     * 제작 설문 검색
     * [GET] /searchs/board
     * @return BaseResponse<GetSearchBoardRes>
     */
    @ResponseBody
    @GetMapping("/surveys")
    @Operation(summary = "설문 게시판 검색", description = "설문 제목")
    private BaseResponse<List<GetSurveyBoardRes>> getSearchBoard(@RequestParam String keyword, @RequestParam("page") int page, @RequestParam("size") int size) {
        List<GetSurveyBoardRes> getSurveyBoardRes = searchService.getSearchBoard(keyword, page, size);

        return new BaseResponse<>(getSurveyBoardRes);
    }

    /**
     * 제작 설문 검색
     * [GET] /searchs/shortforms
     * @return BaseResponse<GetSearchBoardRes>
     */
    @ResponseBody
    @GetMapping("/shortforms")
    @Operation(summary = "짧폼 게시판 검색", description = "짧폼 제목, 설문 제목 기반 검색")
    private BaseResponse<List<GetShortFormListRes>> getSearchShortBoard(@RequestParam String keyword, @RequestParam("page") int page, @RequestParam("size") int size) {
        List<GetShortFormListRes> getShortFormListRes = searchService.getSearchShortBoard(keyword, page, size);

        return new BaseResponse<>(getShortFormListRes);
    }
}
