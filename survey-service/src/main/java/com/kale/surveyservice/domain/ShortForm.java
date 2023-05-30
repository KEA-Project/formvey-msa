package com.kale.surveyservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class ShortForm extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shortform_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String shortQuestion;

    private int shortType;

    private int shortResponse;

    @OneToMany(mappedBy = "shortForm", cascade = CascadeType.REMOVE)
    private List<ShortOption> shortOptions = new ArrayList<>();


    @OneToMany(mappedBy = "shortForm", cascade = CascadeType.REMOVE)
    private List<ShortResult> shortResults = new ArrayList<>();

    public void increaseResponseCnt() {
        this.shortResponse++;
    }
}
