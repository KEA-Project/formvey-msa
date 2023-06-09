package com.kale.surveyservice.repository;

import com.kale.surveyservice.domain.ShortForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortFormRepository extends JpaRepository<ShortForm, Long> {
    Page<ShortForm> findAll(Pageable pageable);

    //내가 만든 설문 제외
    @Query("SELECT sf FROM ShortForm sf WHERE sf.memberId <> ?1 ORDER BY FUNCTION('RAND')")
    Page<ShortForm> findRandom(Long memberId, Pageable pageable);

    // 숏폼 게시판 검색 쿼리
    @Query("SELECT sf FROM ShortForm sf WHERE sf.shortQuestion LIKE %:keyword% OR sf.survey.surveyTitle LIKE %:keyword%")
    Page<ShortForm> findAllBySearchTitle(String keyword, Pageable pageable);

    // 숏폼 검색 페이지 쿼리
    @Query("SELECT sf FROM ShortForm sf WHERE sf.shortQuestion LIKE %:keyword% OR sf.survey.surveyTitle LIKE %:keyword%")
    List<ShortForm> findAllBySearch(String keyword);

}
