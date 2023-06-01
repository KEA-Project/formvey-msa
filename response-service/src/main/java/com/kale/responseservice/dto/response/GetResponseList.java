package com.kale.responseservice.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetResponseList {
    private List<GetResponseListRes> getResponseListRes = new ArrayList<>();

    private int totalPageCnt;

    private int releasedPageCnt;

    private int closedPageCnt;
}
