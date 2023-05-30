package com.kale.surveyservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private int questionIdx;

    private String questionTitle;

    private int type;

    private int isEssential;

    private int isShort;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Choice> choices = new ArrayList<>();
}
