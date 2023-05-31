package com.kale.responseservice.repository;

import com.kale.responseservice.domain.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    //응답 설문 리스트 조회 (페이징)
    @Query("SELECT r FROM Response r join r.memberId m where m=:id and r.status=1")
    Page<Response> findAllByMemberId(Long id, Pageable pageable);

    //응답 설문 리스트 조회
    @Query("SELECT r FROM Response r join r.memberId m where m=:id and r.status=1")
    List<Response> findAllByMemberId(Long id);

    // 상태별 전체 응답 설문 개수 조회
    @Query("SELECT r FROM Response r join r.memberId m where m=:id and r.survey.status=:status")
    List<Response> findAllByStatus(Long id, int status);

    // 이전에 응답한 적이 있는지 조회
    @Query("SELECT r FROM Response r join r.memberId m join r.surveyId s where m=:memberId and s=:surveyId")
    Response findExistResponse(Long memberId, Long surveyId);

    // 개별 응답 조회
    Page<Response> findAllBySurveyId(Long surveyId, Pageable pageable);

    //설문 참여자 리스트 조회
    @Query("SELECT r FROM Response r where r.surveyId=:id")
    List<Response> findBySurveyId(Long id);
}
