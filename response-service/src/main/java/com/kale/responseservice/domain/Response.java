package com.kale.responseservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class Response extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Long id;
    private LocalDateTime responseDate;

    @OneToMany(mappedBy = "response", cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();

    private Long memberId;

    private Long surveyId;

    public void updateStatus(int i) {
        setStatus(i);
    }

//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;
//
//    @ManyToOne
//    @JoinColumn(name = "survey_id")
//    private Survey survey;
//
//    public void update(PostResponseReq dto, Member member, Survey survey){
//        this.member=member;
//        this.survey=survey;
//        this.responseDate=dto.getResponseDate();
//    }
}
