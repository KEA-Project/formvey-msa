//package com.kale.responseservice.service.shortResult;
//
//import com.kale.responseservice.client.MemberServiceClient;
//import com.kale.responseservice.client.SurveyServiceClient;
//import com.kale.responseservice.common.BaseException;
//import com.kale.responseservice.domain.ShortResult;
//import com.kale.responseservice.dto.client.GetClientShortFormRes;
//import com.kale.responseservice.dto.client.GetMemberRes;
//import com.kale.responseservice.dto.client.GetSurveyRes;
//import com.kale.responseservice.dto.shortResult.GetShortResultBoardRes;
//import com.kale.responseservice.dto.shortResult.PostShortResultReq;
//import com.kale.responseservice.repository.ShortResultRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.kale.responseservice.common.BaseResponseStatus.SHORTFORMS_LACKING_POINT;
//
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class ShortResultService {
//    private final ShortResultRepository shortResultRepository;
//    private final SurveyServiceClient surveyServiceClient;
//    private final MemberServiceClient memberServiceClient;
//
//    /**
//     * 짧폼 해금
//     */
//    public void responseShortResult(Long shortFormId, Long memberId) {
//        GetMemberRes member = memberServiceClient.getMemberInfo(memberId).getResult();
//        GetClientShortFormRes shortForm = surveyServiceClient.getShortFormById(shortFormId).getResult();
//
//        // 해금하면 사용자 point 차감
//        if (member.getPoint() < 20) {
//            throw new BaseException(SHORTFORMS_LACKING_POINT);
//        } else {
//            memberServiceClient.incrementPoint(memberId, -20);
//        }
//        // 짧폼 해금
//        shortResultRepository.save(PostShortResultReq.toEntity(memberId, shortFormId));
//    }
//
//    /**
//     * 해금한 짧폼 리스트 조회
//     */
//    public List<GetShortResultBoardRes> getShortResultBoard(int page, int size, Long memberId) {
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
//        Page<ShortResult> boardShortResults = shortResultRepository.findAllByMember(memberId, pageRequest);
//        List<GetShortResultBoardRes> shortResults = new ArrayList<>();
//        GetSurveyRes survey = surveyServiceClient.getSurveyById()
//        int totalPages = shortResultRepository.findAllByMember(memberId).size();
//
//        for (ShortResult shortResult : boardShortResults) {
//            GetShortResultBoardRes dto = new GetShortResultBoardRes(shortResult.getShortFormId(), shortResult.getShortForm().getSurvey().getSurveyTitle(), shortResult.getId(), shortResult.getShortForm().getId(), shortResult.getShortForm().getShortQuestion(), shortResult.getShortForm().getShortType(), shortResult.getShortForm().getShortResponse(), totalPages);
//            shortResults.add(dto);
//        }
//
//        return shortResults;
//    }
//}
//
//