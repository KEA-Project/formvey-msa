package com.kale.surveyservice.service;

import com.kale.surveyservice.*;
import com.kale.surveyservice.client.MemberServiceFeignClient;
import com.kale.surveyservice.client.ResponseServiceFeignClient;
import com.kale.surveyservice.common.BaseException;
import com.kale.surveyservice.domain.*;
import com.kale.surveyservice.dto.choice.GetChoiceInfoRes;
import com.kale.surveyservice.dto.choice.PostChoiceReq;
import com.kale.surveyservice.dto.question.GetQuestionInfoRes;
import com.kale.surveyservice.dto.question.PostQuestionReq;
import com.kale.surveyservice.dto.response.GetResponseListRes;
import com.kale.surveyservice.dto.response.GetShortResponseListRes;
import com.kale.surveyservice.dto.shortForm.PostShortFormReq;
import com.kale.surveyservice.dto.shortOption.PostShortOptionReq;
import com.kale.surveyservice.dto.survey.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.kale.surveyservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.kale.surveyservice.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final ShortFormRepository shortFormRepository;
    private final ShortOptionRepository shortOptionRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;

    private final ResponseServiceFeignClient responseServiceFeignClient;


    /**
     * 설문 첫 생성 컨트롤 메서드 (status = 1 -> 임시저장 / status = 2 -> 배포)
     */
    public PostSurveyRes createSurvey(PostSurveyReq dto, int status) { // 1 -> 짧폼 저장 x
        // 설문 제목을 입력하지 않았을 경우
        if (status == 2 && dto.getSurveyTitle() == null)
            throw new BaseException(SURVEYS_EMPTY_SURVEY_TITLE);

        // 질문이 한 개도 없는 경우
        if (status == 2 && dto.getQuestions() == null)
            throw new BaseException(SURVEYS_EMPTY_QUESTION);

        // 설문 시작 날짜를 입력하지 않았을 경우
        if (status == 2 && dto.getStartDate() == null)
            throw new BaseException(SURVEYS_EMPTY_SURVEY_START_DATE);

        // 설문 종료 날짜를 입력하지 않았을 경우
        if (status == 2 && dto.getEndDate() == null)
            throw new BaseException(SURVEYS_EMPTY_SURVEY_END_DATE);

        Survey survey = PostSurveyReq.toEntity(dto);
        survey.setStatus(status);

        survey = surveyRepository.save(survey); // 본 설문 저장

        return setQuestion(dto, survey, status);
    }

    /**
     * 존재하는 설문 컨트롤 메서드 (status = 1 -> 임시저장 / status = 2 -> 배포)
     */
    public PostSurveyRes updateSurvey(Long surveyId, PostSurveyReq dto, int status) { // 1 -> 짧폼 저장 x
        // 설문 제목을 입력하지 않았을 경우
        if (status == 2 && dto.getSurveyTitle() == null)
            throw new BaseException(SURVEYS_EMPTY_SURVEY_TITLE);

        // 질문이 한 개도 없는 경우
        if (status == 2 && dto.getQuestions() == null)
            throw new BaseException(SURVEYS_EMPTY_QUESTION);

        // 설문 시작 날짜를 입력하지 않았을 경우
        if (status == 2 && dto.getStartDate() == null)
            throw new BaseException(SURVEYS_EMPTY_SURVEY_START_DATE);

        // 설문 시작 날짜를 입력하지 않았을 경우
        if (status == 2 && dto.getEndDate() == null)
            throw new BaseException(SURVEYS_EMPTY_SURVEY_END_DATE);

        Survey survey = surveyRepository.findById(surveyId).get();
        List<Question> questions = questionRepository.findBySurveyId(surveyId);

        questionRepository.deleteAll(questions);
        survey.update(dto);
        survey.setStatus(status);
        surveyRepository.save(survey);

        return setQuestion(dto, survey, status);
    }

    private PostSurveyRes setQuestion(PostSurveyReq dto, Survey survey, int status) {
        for (PostQuestionReq postQuestionReq : dto.getQuestions()) {
            Question question = PostQuestionReq.toEntity(survey, postQuestionReq);
            question = questionRepository.save(question);

            if (status == 2) {
                if (question.getIsShort() == 1) { // 질문 엔티티의 isShort가 1이면 짧폼 저장
                    PostShortFormReq postShortFormReq = new PostShortFormReq(question.getQuestionTitle(), survey.getMemberId(), question.getType(), 0,  postQuestionReq.getChoices());
                    ShortForm shortForm = PostShortFormReq.toEntity(survey, postShortFormReq);
                    shortFormRepository.save(shortForm);

                    if (postShortFormReq.getShortOptions() != null) {
                        for (PostChoiceReq postChoiceReq : postShortFormReq.getShortOptions()) {
                            ShortOption shortOption = PostShortOptionReq.toEntity(shortForm, new PostShortOptionReq(postChoiceReq.getChoiceIdx(), postChoiceReq.getChoiceContent()));
                            shortOptionRepository.save(shortOption);
                        }
                    }
                }
            }

            // 주관식이 아닌 객관식일 경우
            if (postQuestionReq.getChoices() != null) {
                for (PostChoiceReq postChoiceReq : postQuestionReq.getChoices()) {
                    Choice choice = PostChoiceReq.toEntity(question, postChoiceReq);
                    choiceRepository.save(choice);
                }
            }
        }
        return new PostSurveyRes(survey.getId());
    }

    /**
     * 설문 삭제
     */
    public String deleteSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).get();
        surveyRepository.delete(survey); //설문과 관련된 모든 것 삭제

        //응답 서비스로 요청
        String result =responseServiceFeignClient.deleteResponses(surveyId).getResult();

        return result;
    }

    /**
     * 게시판 조회
     */
    public List<GetSurveyBoardRes> getSurveyBoard(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending()); // 페이징 처리 id 내림차순
        Page<Survey> boardSurveys = surveyRepository.findPublicSurvey(pageRequest);
        List<GetSurveyBoardRes> surveys = new ArrayList<>();

        int totalPages = surveyRepository.findAll().size();

        for (Survey survey : boardSurveys) {
            LocalDateTime nowDate = LocalDateTime.now();
            LocalDateTime endDate = survey.getEndDate();
            int remainDay = 0;

            if (endDate != null)
                remainDay = (int) ChronoUnit.DAYS.between(nowDate, endDate);

            //멤버 서비스로 api 요청
            String nickname=memberServiceFeignClient.getInfoSub(survey.getMemberId()).getResult().getNickname();
            GetSurveyBoardRes dto = new GetSurveyBoardRes(survey.getId(), survey.getMemberId(), survey.getSurveyTitle(), remainDay, survey.getResponseCnt(), nickname, totalPages);
            surveys.add(dto);
        }
        return surveys;
    }

    /**
     * 제작 설문 리스트 조회
     */
    public GetSurveyList getSurveyList(Long memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending()); // 페이징 처리 id 내림차순
        Page<Survey> sur = surveyRepository.findByMemberId(memberId,pageRequest);
        GetSurveyList surveys = new GetSurveyList();

        int totalPageCnt = surveyRepository.findByMemberId(memberId).size();
        int unReleasedPageCnt = surveyRepository.findAllByStatus(memberId, 1).size();
        int releasedPageCnt = surveyRepository.findAllByStatus(memberId, 2).size();
        int closedPageCnt = surveyRepository.findAllByStatus(memberId, 3).size();

        surveys.setTotalPageCnt(totalPageCnt);
        surveys.setReleasedPageCnt(releasedPageCnt);
        surveys.setUnReleasedPageCnt(unReleasedPageCnt);
        surveys.setClosedPageCnt(closedPageCnt);

        for (Survey survey : sur) {
            LocalDateTime nowDate = LocalDateTime.now();
            LocalDateTime endDate = survey.getEndDate();
            int remainDay = 0;

            if (endDate != null)
                remainDay = (int) ChronoUnit.DAYS.between(nowDate, endDate);

            GetSurveyListRes dto = new GetSurveyListRes(survey.getId(), survey.getSurveyTitle(), survey.getSurveyContent(),survey.getEndDate().toString(),
                    remainDay, survey.getResponseCnt(),survey.getStatus());
            surveys.getGetSurveyListRes().add(dto);
        }
        return surveys;
    }

    /**
     * 설문 내용 조회
     */
    public GetSurveyInfoRes getSurveyInfo(Long surveyId) {
        // 해당 설문 id가 존재하지 않을 때
        if (surveyRepository.findById(surveyId).isEmpty())
            throw new BaseException(SURVEYS_EMPTY_SURVEY_ID);

        Survey survey = surveyRepository.findById(surveyId).get();

        List<GetQuestionInfoRes> questions = survey.getQuestions().stream()
                .map(question -> {
                    List<GetChoiceInfoRes> choices = question.getChoices().stream()
                            .map(choice -> new GetChoiceInfoRes(choice.getId(),choice.getChoiceIndex(), choice.getChoiceContent()))
                            .collect(Collectors.toList());
                    return new GetQuestionInfoRes(question.getId(),question.getQuestionIdx(), question.getQuestionTitle(),
                            question.getType(), question.getIsEssential(), question.getIsShort(), choices);
                })
                .collect(Collectors.toList());

        return new GetSurveyInfoRes(survey.getMemberId(),survey.getSurveyTitle(), survey.getSurveyContent(), survey.getStartDate().toString(), survey.getEndDate().toString(),
                survey.getResponseCnt(), survey.getIsAnonymous(), survey.getIsPublic(), survey.getExitUrl(), survey.getStatus(),questions);
    }

    /**
     * 도넛 차트 조회
     */
    public GetSurveyChartRes getSurveyChart(Long memberId) {
        GetSurveyChartRes getSurveyChartRes = new GetSurveyChartRes();
        getSurveyChartRes.setCreateSurveyCnt(surveyRepository.findByMemberId(memberId).size()); // 제작 설문 수

        //응답 서비스 조회 요청
        List<GetResponseListRes> getResponseListRes=responseServiceFeignClient.getResCount(memberId).getResult();
        getSurveyChartRes.setResponseCnt(getResponseListRes.size());

        //숏폼 응답 서비스 조회 요청
        List<GetShortResponseListRes> getShortResponseListRes=responseServiceFeignClient.getShortResCount(memberId).getResult();
        getSurveyChartRes.setShortFormResponseCnt(getShortResponseListRes.size());
        getSurveyChartRes.setUnReleasedSurveyCnt(surveyRepository.findAllByStatus(memberId, 1).size());
        getSurveyChartRes.setReleasedSurveyCnt(surveyRepository.findAllByStatus(memberId, 2).size());
        getSurveyChartRes.setClosedSurveyCnt(surveyRepository.findAllByStatus(memberId, 3).size());

        return getSurveyChartRes;
    }
}