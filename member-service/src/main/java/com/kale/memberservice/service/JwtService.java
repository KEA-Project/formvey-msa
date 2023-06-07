package com.kale.memberservice.service;
import com.kale.memberservice.common.BaseException;
import com.kale.memberservice.common.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Date;
import static com.kale.memberservice.common.BaseResponseStatus.*;

@Service
public class JwtService {

    /*
    JWT 생성
    @param userIdx
    @return String
     */
    public String createJwt(Long userIdx){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*30)))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }

    /*
    Refresh token 생성
    @param userIdx
    @return String
    */
    public String createRef(Long userIdx){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*7)))
                .signWith(SignatureAlgorithm.HS256, Secret.REFRESH_SECRET_KEY)
                .compact();
    }

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /*
    JWT에서 userIdx 추출
    @return Long
    @throws BaseException
     */
    public Long getUserIdx() throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return Long.valueOf(claims.getBody().get("userIdx",Integer.class));
    }

    /*
    Refresh token에서 유효시간 추출
    @return int
    @throws BaseException
    */
    public Date getRefExp() throws BaseException{
        //1. JWT 추출
        String refreshToken = getJwt();
        if(refreshToken == null || refreshToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.REFRESH_SECRET_KEY)
                    .parseClaimsJws(refreshToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. 유효시간 추출
        Date date=claims.getBody().getExpiration();
        return date;
    }

}
