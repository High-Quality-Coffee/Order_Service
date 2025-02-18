package com.teamsparta14.order_service.user.service;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.global.response.ResponseCode;
import com.teamsparta14.order_service.user.entity.RefreshEntity;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import com.teamsparta14.order_service.user.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenReissueService {

    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;

    //토큰 재발급
    public ResponseEntity<ApiResponse<String>> token_reissue(HttpServletRequest request, HttpServletResponse response){
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            //response status code
            return ResponseEntity.badRequest().body(ApiResponse.fail(ResponseCode.BAD_REQUEST,"유요한 계정이 아닙니다."));
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return ResponseEntity.badRequest().body(ApiResponse.fail(ResponseCode.BAD_REQUEST,"로그인 시간이 만료되었습니다."));
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return ResponseEntity.badRequest().body(ApiResponse.fail(ResponseCode.BAD_REQUEST,"유요한 계정이 아닙니다."));
        }

        //토큰이 DB에 존재하는지 파악
        Boolean isExist = refreshRepository.existsByRefresh(refresh);

        //토큰이 존재하지 않는다면,
        if(!isExist){
            return ResponseEntity.badRequest().body(ApiResponse.fail(ResponseCode.BAD_REQUEST,"유요한 리프레시 토큰이 아닙니다."));
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //새로운 토큰 생성
        String newAccess = jwtUtil.createJwt("access", username, role,600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role,86400000L);

        //기존 refresh 토큰을 DB에서 삭제 후 새로운 refresh 토큰을 저장한다
        refreshRepository.deleteByRefresh(refresh);
        RefreshTokenSave(username, newRefresh, 86400000L);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh",newRefresh));

        return ResponseEntity.ok().body(ApiResponse.success("토큰 재발급이 완료되었습니다."));

    }


    //리프레시토큰 저장 로직
    public void RefreshTokenSave(String username, String refresh,Long expiredMs){
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
