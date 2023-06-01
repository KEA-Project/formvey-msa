package com.kale.surveyservice.service;


import com.kale.surveyservice.client.ResponseServiceFeignClient;
import com.kale.surveyservice.common.BaseException;
import com.kale.surveyservice.domain.*;
import com.kale.surveyservice.dto.response.GetShortResponseListRes;
import com.kale.surveyservice.dto.shortForm.*;
import com.kale.surveyservice.dto.shortOption.GetShortOptionRes;
import com.kale.surveyservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.kale.surveyservice.common.BaseResponseStatus.*;


@Service
@RequiredArgsConstructor
@Transactional
public class ShortFormService {
    private final ShortFormRepository shortFormRepository;
    private final ShortResultRepository shortResultRepository;

    private final ResponseServiceFeignClient responseServiceFeignClient;

    /**
     * 짧폼 리스트 조회
     */
    public List<GetShortFormListRes> getShortFormList (int page, int size, Long memberId){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ShortForm> boardShortForms = shortFormRepository.findAll(pageRequest);
        List<GetShortFormListRes> shortForms = new ArrayList<>();

        int totalPages = shortFormRepository.findAll().size();

        // 해금 여부 확인
        List<Long> resultList = shortResultRepository.findSurveyIdByMember(memberId);
        int resultStatus;

        for(ShortForm shortForm : boardShortForms){
            if (resultList.contains(shortForm.getId()))
                resultStatus = 1;
            else
                resultStatus = 0;

            GetShortFormListRes dto  = new GetShortFormListRes(shortForm.getSurvey().getId(), shortForm.getSurvey().getSurveyTitle(), shortForm.getId(), shortForm.getShortQuestion(), shortForm.getShortType(), shortForm.getShortResponse(), totalPages, resultStatus);

            shortForms.add(dto);
        }

        return shortForms;
    }

    /**
     * 짧폼 내용 조회
     */
    public GetShortFormRes getShortForm(Long shortFormID){

        if(shortFormRepository.findById(shortFormID).isEmpty())
            throw new BaseException(SHORTFORMS_EMPTY_SHORTFORM_ID);

        ShortForm shortForm = shortFormRepository.findById(shortFormID).get();

        List<GetShortOptionRes> options = shortForm.getShortOptions().stream()
                .map(shortOption -> new GetShortOptionRes(shortOption.getId(), shortOption.getShortIndex(), shortOption.getShortContent()))
                .collect(Collectors.toList());

        return new GetShortFormRes(shortForm.getSurvey().getId(), shortForm.getSurvey().getSurveyTitle(), shortForm.getShortQuestion(), shortForm.getShortType(), options);
    }

    /**
     * 짧폼 메인 조회
     */
    public GetShortFormMainRes getShortFormMain(Long memberId) {
        ShortForm shortForm = shortFormRepository.findRandom(memberId, PageRequest.of(0, 1)).stream().findFirst().orElseThrow(() -> new BaseException(DATABASE_ERROR));

        while(true) {
            //이미 응답 했는지 응답 서비스에 api 요청
            //짧폼 답변에 없으면
            if(responseServiceFeignClient.existShortResponse(shortForm.getMemberId(), shortForm.getId()).getResult().equals("none"))
                break;
            //내가 만든 숏폼 제외 랜덤 뽑기
            shortForm = shortFormRepository.findRandom(memberId, PageRequest.of(0, 1)).stream().findFirst().orElseThrow(() -> new BaseException(DATABASE_ERROR));
        }

        List<GetShortOptionRes> options = shortForm.getShortOptions().stream()
                .map(shortOption -> new GetShortOptionRes(shortOption.getId(), shortOption.getShortIndex(), shortOption.getShortContent()))
                .collect(Collectors.toList());

        return new GetShortFormMainRes(shortForm.getSurvey().getId(), shortForm.getSurvey().getSurveyTitle(), shortForm.getId(), shortForm.getShortQuestion(), shortForm.getShortType(), options);
    }
}
