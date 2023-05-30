package com.kale.surveyservice.repository;

import com.kale.surveyservice.domain.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    //제작 설문 리스트 조회
    @Query("SELECT s FROM Survey s where s.memberId=:id")
    Page<Survey> findByMemberId(Long id, Pageable pageable);

    // 게시판 리스트 조회
    @Query("SELECT s FROM Survey s WHERE s.isPublic = 1")
    Page<Survey> findPublicSurvey(Pageable pageable);

    // 게시판 검색 쿼리
    @Query("SELECT s FROM Survey s WHERE s.surveyTitle LIKE %:keyword%")
    Page<Survey> findAllBySearchTitle(String keyword, Pageable pageable);

    // 게시판 검색 페이지 쿼리
    @Query("SELECT s FROM Survey s WHERE s.surveyTitle LIKE %:keyword%")
    List<Survey> findAllBySearch(String keyword);

    //제작 설문 리스트 조회
    @Query("SELECT s FROM Survey s where s.memberId=:id")
    List<Survey> findByMemberId(Long id);

    // 제작 중인 설문 리스트 조회
    @Query("SELECT s FROM Survey s  WHERE s.memberId=:id AND s.status=:status")
    List<Survey> findAllByStatus(Long id,int status);

    // 마감 기한이 지난 설문 리스트 조회
    List<Survey> findAllByEndDateBefore(LocalDateTime currentDateTime);
}
