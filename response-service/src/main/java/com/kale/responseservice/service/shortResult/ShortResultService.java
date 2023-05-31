package com.kale.responseservice.service.shortResult;

import com.kale.formvey.config.BaseException;
import com.kale.formvey.domain.Member;
import com.kale.formvey.domain.ShortForm;
import com.kale.formvey.domain.ShortResult;
import com.kale.formvey.dto.shortResult.GetShortResultBoardRes;
import com.kale.formvey.dto.shortResult.PostShortResultReq;
import com.kale.formvey.repository.MemberRepository;
import com.kale.formvey.repository.ShortFormRepository;
import com.kale.formvey.repository.ShortResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.kale.formvey.config.BaseResponseStatus.SHORTFORMS_LACKING_POINT;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortResultService {

    private final ShortResultRepository shortResultRepository;
    private final ShortFormRepository shortFormRepository;
    private final MemberRepository memberRepository;

    /**
     * 짧폼 해금
     */
    public void responseShortResult(Long shortFormId, Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        ShortForm shortForm = shortFormRepository.findById(shortFormId).get();

        // 해금하면 사용자 point 차감
        if (member.getPoint() < 20) {
            throw new BaseException( SHORTFORMS_LACKING_POINT);
        } else {
            member.modifySurveyPoint(-20);
            memberRepository.save(member);
        }
        // 짧폼 해금
        shortResultRepository.save(PostShortResultReq.toEntity(member, shortForm));
    }

    /**
     * 해금한 짧폼 리스트 조회
     */
    public List<GetShortResultBoardRes> getShortResultBoard(int page, int size, Long memberId) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ShortResult> boardShortResults = shortResultRepository.findAllByMember(memberId, pageRequest);
        List<GetShortResultBoardRes> shortResults = new ArrayList<>();

        int totalPages = shortResultRepository.findAllByMember(memberId).size();

        for (ShortResult shortResult : boardShortResults) {
            GetShortResultBoardRes dto = new GetShortResultBoardRes(shortResult.getShortForm().getSurvey().getId(), shortResult.getShortForm().getSurvey().getSurveyTitle(), shortResult.getId(), shortResult.getShortForm().getId(), shortResult.getShortForm().getShortQuestion(), shortResult.getShortForm().getShortType(), shortResult.getShortForm().getShortResponse(), totalPages);
            shortResults.add(dto);
        }

        return shortResults;

    }

}
