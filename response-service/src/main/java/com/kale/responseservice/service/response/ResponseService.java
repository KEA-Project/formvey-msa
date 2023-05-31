package com.kale.responseservice.service.response;

import com.kale.responseservice.client.MemberServiceClient;
import com.kale.responseservice.client.SurveyServiceClient;
import com.kale.responseservice.common.BaseException;
import com.kale.responseservice.domain.Answer;
import com.kale.responseservice.domain.Response;
import com.kale.responseservice.dto.client.GetMemberRes;
import com.kale.responseservice.dto.client.GetSurveyRes;
import com.kale.responseservice.dto.response.PostResponseReq;
import com.kale.responseservice.repository.AnswerRepository;
import com.kale.responseservice.repository.ResponseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

//    private final MemberRepository memberRepository;
//    private final SurveyRepository surveyRepository;
//    private final QuestionRepository questionRepository;
//    private final ChoiceRepository choiceRepository;

    /**
     * 설문 응답
     */
    public void responseSurvey(PostResponseReq dto, Long surveyId, Long memberId) {
        GetMemberRes getMemberRes = memberServiceClient.getMemberInfo(memberId).getResult();
        GetSurveyRes getSurveyRes = surveyServiceClient.getSurveyById(surveyId).getResult();

        // 중복 응답 방지
        if (responseRepository.findExistResponse(memberId, surveyId) != null)
            throw new BaseException(RESPONSE_EXIST_SURVEY);

        // 응답자가 본인 설문에 응답하는 경우
        if (survey.getMember().getId().equals(memberId))
            throw new BaseException(RESPONSE_OWN_SURVEY);

        //응답 등록
        Response response = PostResponseReq.toEntity(member, survey, dto);
        response = responseRepository.save(response);

        member.modifySurveyPoint(5);

        survey.increaseResponseCnt(); // 응답 수 증가
        surveyRepository.save(survey);
        
        List<Answer> answer = new ArrayList<>();

        //답변 등록
        for (int i = 0; i < dto.getAnswers().size(); i++) {
            Question question=questionRepository.findById(dto.getAnswers().get(i).getQuestionId()).get();
            answer.add(PostAnswerReq.toEntity(question,response,dto.getAnswers().get(i)));
        }
        answerRepository.saveAll(answer);
    }

    /**
     * 설문 응답 수정
     */
    public void updateResponse(PostResponseReq dto,Long surveyId, Long responseId) {
        Member member = memberRepository.findById(dto.getMemberId()).get(); // 설문 응답자
        Survey survey = surveyRepository.findById(surveyId).get(); // 응답하고자 하는 설문
        Response response = responseRepository.findById(responseId).get();//수정할 응답
        List<Answer> answers=answerRepository.findByResponseId(responseId);//수정할 답변들

        //답변 리스트 초기화
        answerRepository.deleteAll(answers);
        response.update(dto,member,survey);
        response = responseRepository.save(response);

        //답변 등록
        for (int i = 0; i < dto.getAnswers().size(); i++) {
            Question question=questionRepository.findById(dto.getAnswers().get(i).getQuestionId()).get();
            List<Answer> answer=new ArrayList<>();
            answer.add(PostAnswerReq.toEntity(question,response,dto.getAnswers().get(i)));
            answerRepository.saveAll(answer);
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
     * 응답 설문 리스트 조회
     */
    public GetResponseList getResponseList(Long memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending()); // 페이징 처리 id 내림차순
        Page<Response> res = responseRepository.findAllByMemberId(memberId,pageRequest);
        GetResponseList responses = new GetResponseList();

        int totalPageCnt = responseRepository.findAllByMemberId(memberId).size();
        int releasedPageCnt = responseRepository.findAllByStatus(memberId, 2).size();
        int closedPageCnt = responseRepository.findAllByStatus(memberId, 3).size();

        responses.setTotalPageCnt(totalPageCnt);
        responses.setReleasedPageCnt(releasedPageCnt);
        responses.setClosedPageCnt(closedPageCnt);

        for (Response response : res) {
            LocalDateTime nowDate = LocalDateTime.now();
            LocalDateTime endDate = response.getSurvey().getEndDate();
            int remainDay = 0;

            if (endDate != null)
                remainDay = (int) ChronoUnit.DAYS.between(nowDate, endDate);

            GetResponseListRes dto = new GetResponseListRes(response.getSurvey().getId(), response.getId(),response.getSurvey().getSurveyTitle(), response.getSurvey().getSurveyContent(),response.getSurvey().getEndDate().toString(),
                    remainDay, response.getSurvey().getStatus());
            responses.getGetResponseListRes().add(dto);
        }
        return responses;
    }

    /**
     * 응답 설문 내용,답변 조회
     */
    public GetResponseInfoRes getResponseInfo(Long responseId) {
        Response response=responseRepository.findById(responseId).get();

        List<GetQuestionInfoRes> questions = response.getSurvey().getQuestions().stream()
                .map(question -> {
                    List<GetChoiceInfoRes> choices = question.getChoices().stream()
                            .map(choice -> new GetChoiceInfoRes(choice.getId(),choice.getChoiceIndex(), choice.getChoiceContent()))
                            .collect(Collectors.toList());
                    return new GetQuestionInfoRes(question.getId(),question.getQuestionIdx(), question.getQuestionTitle(),
                            question.getType(), question.getIsEssential(), question.getIsShort(), choices);
                })
                .collect(Collectors.toList());

        List<GetAnswerRes> answers=response.getAnswers().stream()
                .map(answer -> new GetAnswerRes(answer.getQuestion().getId(), answer.getAnswerContent()))
                .collect(Collectors.toList());

        return new GetResponseInfoRes( response.getSurvey().getId(), response.getSurvey().getSurveyTitle(),  response.getSurvey().getSurveyContent(),  response.getSurvey().getStartDate().toString(),  response.getSurvey().getEndDate().toString(),
                response.getSurvey().getIsAnonymous(), response.getSurvey().getStatus(),questions,answers);
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
            String nickname;

            if (response.getSurvey().getIsAnonymous() == 1) // 익명 설문인 경우
                nickname = "익명";
            else
                nickname = response.getMember().getNickname();

            getResponseIndividualRes.add(new GetResponseIndividualRes(response.getId(), nickname,
                    response.getResponseDate().toLocalDate().toString(), totalPages));
        }
        return getResponseIndividualRes;
    }
    /**
     * 응답 통계 조회
     */
    public List<GetResponseStatisticsRes> getResponseStatistics(Long surveyId) {
        List<GetResponseStatisticsRes> getResponseStatisticsRes = new ArrayList<>();
        List<Question> questions = questionRepository.findBySurveyId(surveyId);

        for (Question question : questions) {
            List<Answer> answers = answerRepository.findByQuestionId(question.getId());
            List<Choice> choices = choiceRepository.findByQuestionId(question.getId());
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

                    for (Choice choice : choices) {
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
                    for (Choice choice : choices) {
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
