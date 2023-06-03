package com.kale.responseservice.service.response;

import com.kale.responseservice.client.MemberServiceClient;
import com.kale.responseservice.client.SurveyServiceClient;
import com.kale.responseservice.common.BaseException;
import com.kale.responseservice.domain.Answer;
import com.kale.responseservice.domain.Response;
import com.kale.responseservice.dto.answer.GetAnswerRes;
import com.kale.responseservice.dto.answer.PostAnswerReq;
import com.kale.responseservice.dto.client.*;
import com.kale.responseservice.dto.response.*;
import com.kale.responseservice.repository.AnswerRepository;
import com.kale.responseservice.repository.ResponseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kale.responseservice.common.BaseResponseStatus.RESPONSE_EXIST_SURVEY;
import static com.kale.responseservice.common.BaseResponseStatus.RESPONSE_OWN_SURVEY;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponseService {
    private final MemberServiceClient memberServiceClient;
    private final SurveyServiceClient surveyServiceClient;
    private final ResponseRepository responseRepository;
    private final AnswerRepository answerRepository;

    /**
     * 설문 응답
     */
    public void responseSurvey(PostResponseReq dto, Long surveyId) {
        Long memberId = dto.getMemberId();
        GetSurveyRes survey = surveyServiceClient.getSurveyById(surveyId).getResult();
        List<Answer> answers = new ArrayList<>();

        // 중복 응답 방지
        if (responseRepository.findExistResponse(memberId, surveyId) != null)
            throw new BaseException(RESPONSE_EXIST_SURVEY);

        // 응답자가 본인 설문에 응답하는 경우
        if (survey.getMemberId().equals(memberId))
            throw new BaseException(RESPONSE_OWN_SURVEY);

        // 응답 등록
        Response response = PostResponseReq.toEntity(memberId, surveyId, dto);
        response = responseRepository.save(response);

        surveyServiceClient.incrementCount(surveyId); // 설문 응답 수 증가
        memberServiceClient.incrementPoint(memberId, 5);

        // 답변 등록
        for (int i = 0; i < dto.getAnswers().size(); i++) {
            answers.add(PostAnswerReq.toEntity(dto.getAnswers().get(i).getQuestionId(),response,dto.getAnswers().get(i)));
        }
        answerRepository.saveAll(answers);
    }

    /**
     * 설문 응답 수정
     */
    public void updateResponse(PostResponseReq dto,Long surveyId, Long responseId) {
        GetMemberRes member = memberServiceClient.getMemberInfo(dto.getMemberId()).getResult();
        GetSurveyRes survey = surveyServiceClient.getSurveyById(surveyId).getResult();
        List<Answer> answer = new ArrayList<>();

        Response response = responseRepository.findById(responseId).get(); // 수정할 응답
        List<Answer> answers = answerRepository.findByResponseId(responseId); // 수정할 답변들

        //답변 리스트 초기화
        answerRepository.deleteAll(answers);
        response.update(dto,member.getId(),survey.getId());
        response = responseRepository.save(response);

        //답변 등록
        for (int i = 0; i < dto.getAnswers().size(); i++) {
            answers.add(PostAnswerReq.toEntity(dto.getAnswers().get(i).getQuestionId(),response,dto.getAnswers().get(i)));
        }
        answerRepository.saveAll(answer);
    }

    /**
     * 응답 삭제 (설문 client)
     */
    public void deleteClientResponse(Long surveyId) {
        //status 0으로 변경
        List<Response> response=responseRepository.findBySurveyId(surveyId);
        for(int i=0;i<response.size();i++) {
            responseRepository.delete(response.get(i));
        }
    }

    /**
     * 응답 삭제
     */
    public void deleteResponse(Long responseId) {
        //status 0으로 변경
        Response response=responseRepository.findById(responseId).get();
        response.updateStatus(0);
        responseRepository.save(response);
    }

    /**
     * 응답 개수 조회 (설문 client)
     */
    public List<GetResponseCountRes> getResponseListCount(Long memberId) {
        List<Response> responses = responseRepository.findAllByMemberId(memberId);
        List<GetResponseCountRes> getResponseCountRes=new ArrayList<>();

        for (Response response : responses) {
            getResponseCountRes.add(new GetResponseCountRes(response.getId()));
        }
        return getResponseCountRes;
    }

    /**
     * 응답 설문 리스트 조회
     */
    public GetResponseList getResponseList(Long memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending()); // 페이징 처리 id 내림차순
        Page<Response> res = responseRepository.findAllByMemberId(memberId, pageRequest);
        GetResponseList responses = new GetResponseList();
        List<GetSurveyRes> releasedSurveys = new ArrayList<>(); // 배포 중인 설문 모음
        List<GetSurveyRes> closedSurveys = new ArrayList<>(); // 마감된 설문 모음

        List<Response> responseList = responseRepository.findAllByStatus(memberId);

        for (Response r : responseList) { // 유저가 응답한 설문 모음집
            GetSurveyRes s = surveyServiceClient.getSurveyById(r.getSurveyId()).getResult();
            if (s.getStatus() == 2) // 배포 중인 설문
                releasedSurveys.add(s);
            else
                closedSurveys.add(s);
        }

        int totalPageCnt = responseRepository.findAllByMemberId(memberId).size();
        int releasedPageCnt = releasedSurveys.size();
        int closedPageCnt = closedSurveys.size();

        responses.setTotalPageCnt(totalPageCnt);
        responses.setReleasedPageCnt(releasedPageCnt);
        responses.setClosedPageCnt(closedPageCnt);

        for (Response response : res) {
            GetSurveyRes s = surveyServiceClient.getSurveyById(response.getSurveyId()).getResult();
            LocalDateTime nowDate = LocalDateTime.now();
            LocalDateTime endDate = s.getEndDate();
            int remainDay = 0;

            if (endDate != null)
                remainDay = (int) ChronoUnit.DAYS.between(nowDate, endDate);

            GetResponseListRes dto = new GetResponseListRes(response.getSurveyId(), response.getId(), s.getSurveyTitle(), s.getSurveyContent(), s.getEndDate().toString(),
                    remainDay, s.getStatus());
            responses.getGetResponseListRes().add(dto);
        }
        return responses;
    }

    /**
     * 응답 설문 내용,답변 조회
     */

    public GetResponseInfoRes getResponseInfo(Long responseId) {
        Response response = responseRepository.findById(responseId).get();
        List<GetQuestionRes> questions = surveyServiceClient.getQuestionBySurveyId(response.getSurveyId()).getResult();
        GetSurveyRes survey = surveyServiceClient.getSurveyById(response.getSurveyId()).getResult();
        List<GetQuestionInfoRes> qInfos = new ArrayList<>();

        for (GetQuestionRes q : questions) {
            List<GetChoiceInfoRes> cInfos = new ArrayList<>();
            for (GetChoiceRes c : q.getChoices()) {
                cInfos.add(new GetChoiceInfoRes(c.getId(), c.getChoiceIndex(), c.getChoiceContent()));
            }
            qInfos.add(new GetQuestionInfoRes(q.getId(), q.getQuestionIdx(), q.getQuestionTitle(), q.getType(),
                    q.getIsEssential(), q.getIsShort(), cInfos));
        }

        List<GetAnswerRes> answers = response.getAnswers().stream()
                .map(answer -> new GetAnswerRes(answer.getQuestionId(), answer.getAnswerContent())).toList();

        GetResponseInfoRes dto = new GetResponseInfoRes(survey.getId(), survey.getSurveyTitle(), survey.getSurveyContent(),
                survey.getStartDate().toString(), survey.getEndDate().toString(), survey.getIsAnonymous(),
                survey.getStatus(), qInfos, answers);

        return dto;
    }

    /**
     * 제작 설문 개별 응답 정보 조회 (client 요청)
     */
    public List<GetResponseListInfoRes> getResponseIndividualInfo(Long surveyId) {
        List<Response> responses=responseRepository.findBySurveyId(surveyId);
        List<GetResponseListInfoRes> getResponseListInfoRes=new ArrayList<>();

        for (Response response : responses) {
            getResponseListInfoRes.add(new GetResponseListInfoRes(response.getId(), response.getMemberId()));
        }
        return getResponseListInfoRes;
    }

    /**
     * 개별 응답 조회
     */
    public List<GetResponseIndividualRes> getResponseIndividual(Long surveyId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending()); // 페이징 처리 id 오름차순
        List<GetResponseIndividualRes> getResponseIndividualRes = new ArrayList<>();
        Page<Response> responses = responseRepository.findAllBySurveyId(surveyId, pageRequest);

        int totalPages = responses.getTotalPages();

        for (Response response : responses) {
            GetSurveyRes survey = surveyServiceClient.getSurveyById(response.getSurveyId()).getResult();
            GetMemberRes member = memberServiceClient.getMemberInfo(response.getMemberId()).getResult();
            String nickname;

            if (survey.getIsAnonymous() == 1) // 익명 설문인 경우
                nickname = "익명";
            else
                nickname = member.getNickname();

            getResponseIndividualRes.add(new GetResponseIndividualRes(response.getId(), response.getMemberId(), nickname,
                    response.getResponseDate().toLocalDate().toString(), totalPages));
        }
        return getResponseIndividualRes;
    }
    /**
     * 응답 통계 조회
     */
    public List<GetResponseStatisticsRes> getResponseStatistics(Long surveyId) {
        List<GetResponseStatisticsRes> getResponseStatisticsRes = new ArrayList<>();
        List<GetQuestionRes> questions = surveyServiceClient.getQuestionBySurveyId(surveyId).getResult();

        for (GetQuestionRes question : questions) {
            List<Answer> answers = answerRepository.findByQuestionId(question.getId());
            List<GetChoiceRes> choices = question.getChoices();
            List<MultipleChoiceInfo> multipleChoiceInfos= new ArrayList<>();
            List<String> subjectiveAnswers = new ArrayList<>();
            int[] multipleChoiceCnt = new int[choices.size()];
            Arrays.fill(multipleChoiceCnt, 0);

            if (question.getType() == 2) { // 주관식이면 주관식 답변 리스트 반환 객관식 답변은 null
                for (Answer answer : answers) {
                    String answerContent = answer.getAnswerContent();
                    subjectiveAnswers.add(answerContent);
                }
                getResponseStatisticsRes.add(new GetResponseStatisticsRes(question.getId(), question.getQuestionIdx(), question.getQuestionTitle(), null, subjectiveAnswers));
            }
            else if (question.getType() == 1){ // 다중 객관식 답변 리스트 반환 - choices 크기 만큼의 int배열 선언, ,answerContent랑 choiceContent랑 비교해서 일치하면 해당 인덱스 int값 상승
                for (Answer answer : answers) {
                    String answerContent = answer.getAnswerContent();
                    String contents = answerContent.substring(1, answerContent.length() - 1);
                    String[] contentList = contents.split(", "); // 여러개 응답 파싱

                    for (GetChoiceRes choice : choices) {
                        for (String content : contentList) {
                            if (content.equals(choice.getChoiceContent())) {
                                multipleChoiceCnt[choice.getChoiceIndex()]++;
                            }
                        }
                    }
                }
                for (int i = 0; i < choices.size(); i++) {
                    MultipleChoiceInfo multipleChoiceInfo = new MultipleChoiceInfo(choices.get(i).getChoiceIndex(), choices.get(i).getChoiceContent(), multipleChoiceCnt[i]);
                    multipleChoiceInfos.add(multipleChoiceInfo);
                }
                getResponseStatisticsRes.add(new GetResponseStatisticsRes(question.getId(), question.getQuestionIdx(), question.getQuestionTitle(), multipleChoiceInfos, null));
            } else {
                for (Answer answer : answers) {
                    String answerContent = answer.getAnswerContent();
                    for (GetChoiceRes choice : choices) {
                        if (answerContent.equals(choice.getChoiceContent())) {
                            multipleChoiceCnt[choice.getChoiceIndex()]++;
                        }
                    }
                }
                for (int i = 0; i < choices.size(); i++) {
                    MultipleChoiceInfo multipleChoiceInfo = new MultipleChoiceInfo(choices.get(i).getChoiceIndex(), choices.get(i).getChoiceContent(), multipleChoiceCnt[i]);
                    multipleChoiceInfos.add(multipleChoiceInfo);
                }
                getResponseStatisticsRes.add(new GetResponseStatisticsRes(question.getId(), question.getQuestionIdx(), question.getQuestionTitle(), multipleChoiceInfos, null));
            }
        }
        return getResponseStatisticsRes;
    }
}
