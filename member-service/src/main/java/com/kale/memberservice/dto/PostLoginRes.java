package com.kale.memberservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostLoginRes {
    private Long id;
    private String jwt;
    private long expiration;
    private String refresh;

}
