package com.kale.memberservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PatchLoginRes {
    private String jwt;
    private long expiration;
}
