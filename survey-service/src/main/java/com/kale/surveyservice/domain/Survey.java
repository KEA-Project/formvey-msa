package com.kale.surveyservice.domain;

import com.kale.surveyservice.dto.survey.PostSurveyReq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class Survey extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    private Long memberId;

    private String surveyTitle;

    private String surveyContent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int responseCnt;

    private int isAnonymous; // 0 -> 익명x, 1 -> 익명 가능

    private int isPublic; // 0 -> 게시판 공개 x -> 1 ->

    private String exitUrl;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.REMOVE)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "survey", cascade = CascadeType.REMOVE)
    private List<ShortForm> shortForms  = new ArrayList<>();

    public void update(PostSurveyReq dto) {
        this.surveyTitle = dto.getSurveyTitle();
        this.memberId = dto.getMemberId();
        this.surveyContent = dto.getSurveyContent();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.responseCnt = 0;
        this.isAnonymous = dto.getIsAnonymous();
        this.isPublic = dto.getIsPublic();
        this.exitUrl = dto.getExitUrl();
    }

    public void increaseResponseCnt() {
        this.responseCnt++;
    }
}
