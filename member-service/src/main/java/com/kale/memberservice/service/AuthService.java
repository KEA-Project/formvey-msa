package com.kale.memberservice.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kale.memberservice.common.BaseException;
import com.kale.memberservice.domain.Member;
import com.kale.memberservice.dto.PostLoginReq;
import com.kale.memberservice.dto.PostLoginRes;
import com.kale.memberservice.dto.PostMemberReq;
import com.kale.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static com.kale.memberservice.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;

    private final JwtService jwtService;

    /**
     * 이메일 로그인 및 jwt 생성
     */
    public PostLoginRes emailLogin(PostLoginReq dto) {
        // 존재하지 않는 이메일
        if (memberRepository.findByEmail(dto.getEmail()).isEmpty())
            throw new BaseException(POST_USERS_EMPTY_EMAIL);

        // 비밀번호가 틀림
        if (!memberRepository.findByEmail(dto.getEmail()).get().getPassword().equals(dto.getPassword()))
            throw new BaseException(POST_USERS_WRONG_PASSWORD);

        //jwt 생성 후 반환
        Member member = memberRepository.findByEmail(dto.getEmail()).get();
        member.updateStatus(1);
        memberRepository.save(member);
        String jwt = jwtService.createJwt(member.getId());
        String ref = jwtService.createRef(member.getId());

        return new PostLoginRes(member.getId(), jwt, System.currentTimeMillis()+1*(1000*60*30), ref);
    }

    /**
     * 카카오 로그인 및 jwt 생성
     */
    public PostLoginRes kakaoLogin(String token) {

        // token으로 사용자 정보 가져오기
        PostMemberReq info = getKakaoInfo(token);

        // 존재하지 않는 이메일(신규 회원)이면 회원가입 자동진행 후 로그인
        if (memberRepository.findByEmail(info.getEmail()).isEmpty()) {

            Member member = PostMemberReq.toEntity(info);
            member = memberRepository.save(member);

            // jwt 생성 후 반환
            String jwt = jwtService.createJwt(member.getId());
            String ref = jwtService.createRef(member.getId());

            return new PostLoginRes(member.getId(), jwt, System.currentTimeMillis()+1*(1000*60*30), ref);
        }

        // 존재하는 이메일이면 로그인 진행
        // jwt 생성 후 반환
        Member member = memberRepository.findByEmail(info.getEmail()).get();
        member.updateStatus(1);
        memberRepository.save(member);

        String jwt = jwtService.createJwt(member.getId());
        String ref = jwtService.createRef(member.getId());

        return new PostLoginRes(member.getId(), jwt, System.currentTimeMillis()+1*(1000*60*30), ref);
    }

    /**
     * 카카오 서버에서 회원가입에 필요한 사용자 정보 가져오기
     */
    public PostMemberReq getKakaoInfo(String token) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        PostMemberReq info = new PostMemberReq();
        String email = "";
        String nickName = "";

        // access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            // Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            // JsonElement element =  Jsonparser.parseString(result);

            // 이메일 가져오기
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }
            System.out.println("email : " + email);

            // 닉네임 가져오기
            nickName = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            System.out.println("email : " + nickName);
            br.close();

            info.setEmail(email);
            info.setNickname(nickName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }


    /**
     * 로그아웃
     */
    public void logout(Long memberId) {
        // 해당 유저 id가 존재하지 않을 때
        if (memberRepository.findById(memberId).isEmpty())
            throw new BaseException(USERS_EMPTY_USER_ID);

        Member member = memberRepository.findById(memberId).get();
        member.updateStatus(0);
        memberRepository.save(member);
    }
}
