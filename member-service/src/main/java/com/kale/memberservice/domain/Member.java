package com.kale.memberservice.domain;

import com.kale.memberservice.dto.PatchMemberReq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String nickname;
    private String password;
    private int point;

    public void update(PatchMemberReq dto) {
        this.nickname = dto.getNickname();
        this.password = dto.getPassword();
    }

    public void updateStatus(int i) {
        setStatus(i);
    }

    public void modifySurveyPoint(int i) {
        this.point += i;
    }
}
