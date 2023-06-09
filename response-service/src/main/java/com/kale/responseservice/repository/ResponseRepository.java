package com.kale.responseservice.repository;

import com.kale.responseservice.domain.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    // 응답 설문 리스트 조회
    @Query("SELECT r FROM Response r where r.memberId=:id")
    List<Response> findAllByStatus(Long id);

    //응답 설문 리스트 조회 (페이징)
    @Query("SELECT r FROM Response r where r.memberId=:id and r.status=1")
    Page<Response> findAllByMemberId(Long id, Pageable pageable);

    //응답 설문 리스트 조회
    @Query("SELECT r FROM Response r where r.memberId=:id and r.status=1")
    List<Response> findAllByMemberId(Long id);

    // 이전에 응답한 적이 있는지 조회
    @Query("SELECT r FROM Response r where r.memberId=:memberId and r.surveyId=:surveyId")
    Response findExistResponse(Long memberId, Long surveyId);

    // 개별 응답 조회
    Page<Response> findAllBySurveyId(Long surveyId, Pageable pageable);

    //설문 참여자 리스트 조회
    @Query("SELECT r FROM Response r where r.surveyId=:id")
    List<Response> findBySurveyId(Long id);
}
