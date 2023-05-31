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
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private String answerContent;

    private Long questionId;

    private Long responseId;

//    @ManyToOne
//    @JoinColumn(name = "question_id")
//    private Question question;
//
//    @ManyToOne
//    @JoinColumn(name = "response_id")
//    private Response response;

}
