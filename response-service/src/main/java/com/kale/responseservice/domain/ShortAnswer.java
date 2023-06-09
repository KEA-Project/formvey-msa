package com.kale.responseservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class ShortAnswer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shortanswer_id")
    private Long id;

    private String shortAnswer;

    private Long shortFormId;

    private Long memberId;

//    @ManyToOne
//    @JoinColumn(name = "shortform_id")
//    private ShortForm shortForm;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;
}
