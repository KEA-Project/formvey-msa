package com.kale.responseservice.dto.client;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetOptionRes {

    private Long id;

    private int shortIndex;

    private String shortContent;
}
