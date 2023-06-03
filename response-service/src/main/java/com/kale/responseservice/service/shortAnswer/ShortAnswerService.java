package com.kale.responseservice.service.shortAnswer;

import com.kale.responseservice.client.MemberServiceClient;
import com.kale.responseservice.client.SurveyServiceClient;
import com.kale.responseservice.common.BaseException;
import com.kale.responseservice.domain.Response;
import com.kale.responseservice.domain.ShortAnswer;
import com.kale.responseservice.dto.client.GetClientShortFormRes;
import com.kale.responseservice.dto.client.GetMemberRes;
import com.kale.responseservice.dto.client.GetResponseCountRes;
import com.kale.responseservice.dto.client.GetShortResponseCountRes;
import com.kale.responseservice.dto.shortAnswer.PostShortAnswerReq;
import com.kale.responseservice.repository.ShortAnswerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.kale.responseservice.common.BaseResponseStatus.RESPONSE_OWN_SHORTFORM;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortAnswerService {
    private final ShortAnswerRepository shortAnswerRepository;
    private final MemberServiceClient memberServiceClient;
    private final SurveyServiceClient surveyServiceClient;

    /**
     * 짧폼 답변
     */
    public void responseShortAnswer(PostShortAnswerReq dto, Long shortFormId, Long memberId) {
        GetMemberRes member = memberServiceClient.getMemberInfo(memberId).getResult(); // 짧폼 응답자
        GetClientShortFormRes shortForm = surveyServiceClient.getShortFormById(shortFormId).getResult(); // 답변하고자 하는 짧폼

        // 응답자가 본인 설문에 응답하는 경우
        if (shortForm.getMemberId().equals(memberId))
            throw new BaseException(RESPONSE_OWN_SHORTFORM);

        // 짧폼 응답 수 증가
        surveyServiceClient.incrementShortCount(shortFormId);

        // 숏폼 답변 등록
        List<ShortAnswer> shortAnswer = new ArrayList<>();
        shortAnswer.add(PostShortAnswerReq.toEntity(memberId, shortFormId, dto));
        shortAnswerRepository.saveAll(shortAnswer);

        // 응답자 point 증가
        int point = dto.getPoint();
        memberServiceClient.incrementPoint(memberId, point);
    }

    /**
     * 짧폼 응답 개수 조회 (설문 client)
     */
    public List<GetShortResponseCountRes> getShortResponseListCount(Long memberId) {
        List<ShortAnswer> responses = shortAnswerRepository.findByMemberId(memberId);
        List<GetShortResponseCountRes> getShortResponseCountRes=new ArrayList<>();

        for (ShortAnswer shortAnswer : responses) {
            getShortResponseCountRes.add(new GetShortResponseCountRes(shortAnswer.getId()));
        }
        return getShortResponseCountRes;
    }

    /**
     * 짧폼에 이미 응답 했는지 여부 (설문 client)
     */
    public String existShortResponse(Long memberId, Long shortformId) {
        String result="exist";
        if(shortAnswerRepository.findExistById(memberId, shortformId).equals(false)){
            result="none";
        }
        return result;
    }
}
