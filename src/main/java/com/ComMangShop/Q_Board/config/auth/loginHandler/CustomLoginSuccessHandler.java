package com.ComMangShop.Q_Board.config.auth.loginHandler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("CustomLoginSuccessHandler's onAuthenticationSuccess! ");
        Collection<? extends GrantedAuthority> collection = authentication.getAuthorities();


        collection.forEach((role) -> {
            try {
                System.out.println("role : " + role.getAuthority());
                String role_str = role.getAuthority();

                if (role_str.equals("ROLE_USER")) {

                    System.out.println("USER 페이지로 이동!");
                    //response.sendRedirect(request.getContextPath()+"/");

                } else if (role_str.equals("ROLE_MEMBER")) {
                    System.out.println("MEMBER 페이지로 이동!");
                    //response.sendRedirect(request.getContextPath()+"/");

                } else if (role_str.equals("ROLE_ADMIN")) {
                    System.out.println("ADMIN 페이지로 이동!");
                    //response.sendRedirect(request.getContextPath()+"/");

                }

                //response.sendRedirect(request.getContextPath()+"/");
                ////////////////////////////////////////////////////////////////

                clearSession(request);

                SavedRequest savedRequest = requestCache.getRequest(request, response);

                /**
                 * prevPage가 존재하는 경우 = 사용자가 직접 /login 경로로 로그인 요청
                 * 기존 Session의 prevPage attribute 제거
                 */
                String prevPage = (String) request.getSession().getAttribute("prevPage");
                if (prevPage != null) {
                    request.getSession().removeAttribute("prevPage");
                }
                // 기본 URI
                String uri = "/";

                /**
                 * savedRequest 존재하는 경우 = 인증 권한이 없는 페이지 접근
                 * Security Filter가 인터셉트하여 savedRequest에 세션 저장
                 */
                if (savedRequest != null) {
                    uri = savedRequest.getRedirectUrl();
                } else if (prevPage != null && !prevPage.equals("")) {
                    // 회원가입 - 로그인으로 넘어온 경우 "/"로 redirect
                    if (prevPage.contains("/user/join")) {
                        uri = "/";
                    } else {
                        uri = prevPage;
                    }
                }

                redirectStrategy.sendRedirect(request, response, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ////////////////////////////////////////////////////////////////

        });


    }

    // 로그인 실패 후 성공 시 남아있는 에러 세션 제거
    protected void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }
}
