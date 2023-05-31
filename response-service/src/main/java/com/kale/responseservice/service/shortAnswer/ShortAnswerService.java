package com.kale.responseservice.service.shortAnswer;

import com.kale.formvey.config.BaseException;
import com.kale.formvey.domain.*;
import com.kale.formvey.dto.shortAnswer.PostShortAnswerReq;
import com.kale.formvey.repository.MemberRepository;
import com.kale.formvey.repository.ShortAnswerRepository;
import com.kale.formvey.repository.ShortFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.kale.formvey.config.BaseResponseStatus.*;
import static com.kale.formvey.config.BaseResponseStatus.RESPONSE_OWN_SHORTFORM;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortAnswerService {
    private final ShortAnswerRepository shortAnswerRepository;
    private final MemberRepository memberRepository;
    private final ShortFormRepository shortFormRepository;

    /**
     * 짧폼 답변
     */
    public void responseShortAnswer(PostShortAnswerReq dto, Long shortFormId, Long memberId) {
        Member member = memberRepository.findById(memberId).get(); // 짧폼 답변자
        ShortForm shortForm = shortFormRepository.findById(shortFormId).get(); // 답변하고자 하는 짧폼

        // 응답자가 본인 설문에 응답하는 경우
        if (shortForm.getSurvey().getMember().getId().equals(memberId))
            throw new BaseException(RESPONSE_OWN_SHORTFORM);

        shortForm.increaseResponseCnt(); //응답 증가
        // 숏폼 답변 등록
        List<ShortAnswer> shortAnswer = new ArrayList<>();
        shortAnswer.add(PostShortAnswerReq.toEntity(member, shortForm, dto));
        shortAnswerRepository.saveAll(shortAnswer);

        // 응답자 point 증가
        int point = dto.getPoint();
        member.modifySurveyPoint(member.getPoint() + point);
        memberRepository.save(member);
    }
}
