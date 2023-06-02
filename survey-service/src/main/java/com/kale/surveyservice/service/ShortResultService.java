package com.kale.surveyservice.service;

import com.kale.surveyservice.client.MemberServiceFeignClient;
import com.kale.surveyservice.common.BaseException;
import com.kale.surveyservice.domain.ShortForm;
import com.kale.surveyservice.domain.ShortResult;
import com.kale.surveyservice.dto.shortResult.GetShortResultBoardRes;
import com.kale.surveyservice.dto.shortResult.PostShortResultReq;
import com.kale.surveyservice.repository.ShortFormRepository;
import com.kale.surveyservice.repository.ShortResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import static com.kale.surveyservice.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortResultService {

    private final ShortResultRepository shortResultRepository;
    private final ShortFormRepository shortFormRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;

    /**
     * 짧폼 해금
     */
    public void responseShortResult(Long shortFormId, Long memberId) {
        //멤버 서비스로 api 요청
        int point=memberServiceFeignClient.getInfoSub(memberId).getResult().getPoint();
        ShortForm shortForm = shortFormRepository.findById(shortFormId).get();

        // 해금하면 사용자 point 차감
        if (point < 20) {
            throw new BaseException( SHORTFORMS_LACKING_POINT);
        } else {
            //멤버 서비스로 포인트 차감 api 요청
           String reduce = memberServiceFeignClient.modifyPoint(memberId).getResult();
        }
        // 짧폼 해금
        shortResultRepository.save(PostShortResultReq.toEntity(memberId, shortForm));
    }

    /**
     * 해금한 짧폼 리스트 조회
     */
    public List<GetShortResultBoardRes> getShortResultBoard(int page, int size, Long memberId) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ShortResult> boardShortResults = shortResultRepository.findAllByMember(memberId, pageRequest);
        List<GetShortResultBoardRes> shortResults = new ArrayList<>();

        int totalPages = shortResultRepository.findAllByMember(memberId).size();

        for (ShortResult shortResult : boardShortResults) {
            GetShortResultBoardRes dto = new GetShortResultBoardRes(shortResult.getShortForm().getSurvey().getId(), shortResult.getShortForm().getSurvey().getSurveyTitle(), shortResult.getId(), shortResult.getShortForm().getId(), shortResult.getShortForm().getShortQuestion(), shortResult.getShortForm().getShortType(), shortResult.getShortForm().getShortResponse(), totalPages);
            shortResults.add(dto);
        }

        return shortResults;

    }

}
