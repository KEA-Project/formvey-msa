package com.kale.responseservice.service.shortAnswer;

import com.kale.responseservice.client.MemberServiceClient;
import com.kale.responseservice.client.SurveyServiceClient;
import com.kale.responseservice.common.BaseException;
import com.kale.responseservice.domain.Response;
import com.kale.responseservice.domain.ShortAnswer;
import com.kale.responseservice.dto.client.*;
import com.kale.responseservice.dto.response.MultipleChoiceInfo;
import com.kale.responseservice.dto.shortAnswer.GetShortStatisticsRes;
import com.kale.responseservice.dto.shortAnswer.PostShortAnswerReq;
import com.kale.responseservice.repository.ShortAnswerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
    public String existShortResponse(Long memberId, Long shortFormId) {
        String result="exist";
        List<ShortAnswer> shortAnswer=shortAnswerRepository.findExistById(memberId, shortFormId);
        if(shortAnswer.isEmpty()){
            result="none";
        }
        return result;
    }


    /**
     * 짧폼 응답 통계 조회 (짧폼 client)
     */

    public GetShortStatisticsRes getShortStatistics(Long shortFormId) {
        GetClientShortFormRes shortForm = surveyServiceClient.getShortFormById(shortFormId).getResult();
        GetSurveyRes survey = surveyServiceClient.getSurveyById(shortForm.getSurveyId()).getResult();

        List<ShortAnswer> answers = shortAnswerRepository.findByShortFormId(shortFormId);

        if (shortForm.getShortType() == 2) { //주관식
            List<String> subjectiveAnswers = new ArrayList<>();
            for (ShortAnswer answer : answers) {
                subjectiveAnswers.add(answer.getShortAnswer());
            }
            return new GetShortStatisticsRes(survey.getSurveyTitle(), shortForm.getShortQuestion(), null, subjectiveAnswers);
        } else if (shortForm.getShortType() == 0) { //단일 선택
            List<GetOptionRes> options = surveyServiceClient.getShortOptionByShortFormId(shortFormId).getResult();
            List<MultipleChoiceInfo> multipleChoiceInfos = new ArrayList<>();

            int[] count = new int[options.size()];
            Arrays.fill(count, 0);

            for (ShortAnswer answer : answers) {
                String content = answer.getShortAnswer().substring(1, answer.getShortAnswer().length() - 1);
                for (GetOptionRes option : options) {
                    if (option.getShortContent().equals(content)) {
                        count[option.getShortIndex()]++;
                    }
                }
            }
            for (int i = 0; i < options.size(); i++) {
                multipleChoiceInfos.add(new MultipleChoiceInfo(options.get(i).getShortIndex(), options.get(i).getShortContent(), count[i]));
            }
            return new GetShortStatisticsRes(survey.getSurveyTitle(), shortForm.getShortQuestion(), multipleChoiceInfos, null);
        } else { //객관식 다중 선택
            List<GetOptionRes> options = surveyServiceClient.getShortOptionByShortFormId(shortFormId).getResult();
            List<MultipleChoiceInfo> multipleChoiceInfos = new ArrayList<>();

            int[] count = new int[options.size()];
            Arrays.fill(count, 0);

            for (ShortAnswer answer : answers) {
                String content = answer.getShortAnswer().substring(1, answer.getShortAnswer().length() - 1);
                String[] contents = content.split(",");
                for (String c : contents) {
                    for (GetOptionRes option : options) {
                        if (option.getShortContent().equals(c)) {
                            count[option.getShortIndex()]++;
                        }
                    }
                }
            }
            for (int i = 0; i < options.size(); i++) {
                multipleChoiceInfos.add(new MultipleChoiceInfo(options.get(i).getShortIndex(), options.get(i).getShortContent(), count[i]));
            }
            return new GetShortStatisticsRes(survey.getSurveyTitle(), shortForm.getShortQuestion(), multipleChoiceInfos, null);

        }
    }


}
