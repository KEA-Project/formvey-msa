package com.kale.surveyservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class Choice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private int choiceIndex;

    private String choiceContent;
}
