package com.inventory.manage.common.filter;

import java.io.IOException;
import java.io.PrintWriter;

import com.inventory.manage.common.util.CookieGenerator;
import com.inventory.manage.common.util.JwtUtil;
import org.springframework.web.filter.GenericFilterBean;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogoutFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    private final CookieGenerator cookieGenerator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // /logout경로로 post요청을 통해 로그아웃이 호출되었는지 검증
        if (!request.getRequestURI().matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (!request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 refresh토큰 추출
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        // 쿠키 존재 여부 check
        if (cookies == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        // refresh토큰 null check, 만료 여부 check
        if (refresh == null || jwtUtil.isExpired(refresh)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            try (PrintWriter writer = response.getWriter()) {
                writer.print("로그아웃에 실패했습니다");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        response.addCookie(cookieGenerator.generateCookie("refresh", null, 0));
        
        response.setStatus(HttpServletResponse.SC_OK);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print("로그아웃이 완료되었습니다");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
