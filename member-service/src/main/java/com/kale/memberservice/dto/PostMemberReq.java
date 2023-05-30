package com.kale.memberservice.dto;

import com.kale.memberservice.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostMemberReq {
    private String email;

    private String nickname;

    private String password;

    public static Member toEntity(PostMemberReq dto){
        return Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .point(0)
                .build();
    }
}
