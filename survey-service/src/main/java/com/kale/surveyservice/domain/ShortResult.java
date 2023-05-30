package com.kale.surveyservice.domain;

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
public class ShortResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shortresult_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shortform_id")
    private ShortForm shortForm;

    private Long memberId;

}
