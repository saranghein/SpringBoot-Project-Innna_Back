package com.inventory.manage.common.filter;

import java.io.PrintWriter;

import com.inventory.manage.common.util.CookieGenerator;
import com.inventory.manage.common.util.JwtGenerator;
import com.inventory.manage.user.entity.User;
import io.jsonwebtoken.io.IOException;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.FilterChain;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final CookieGenerator cookieGenerator;

    // 스프링 시큐리티로 사용자 검증 진행
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        setUsernameParameter("userId");
        String userId = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password);

        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공시 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authentication) throws java.io.IOException {
        String userId = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtGenerator.generateJwt("access", userId, role, 1000 * 60 * 1000L); // 1000분(테스트용)
        String refreshToken = jwtGenerator.generateJwt("refresh", userId, role, 24 * 60 * 60 * 1000L); // 24시간

        // 헤더에 access token 추가
        response.setHeader(HttpHeaders.AUTHORIZATION, "bearer " + accessToken);

        // 쿠키에 refresh token 추가
        response.addCookie(cookieGenerator.generateCookie("refresh", refreshToken, 60 * 60 * 24));

        response.setStatus(HttpStatus.OK.value());

        // 로그인 성공 시 로그인 한 사용자의 닉네임, 군종을 반환
        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();

        String userJsonResponse = "{\"nickname\" : \"" + nickname
                + "\",\"role\" : \"" + role + "\"}";

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(userJsonResponse);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws java.io.IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print("아이디 또는 비밀번호가 일치하지 않습니다");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
