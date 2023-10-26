package com.ComMangShop.Q_Board.config.auth.loginHandler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        HttpSession session = request.getSession();
        session.setAttribute("prevPage", request.getRequestURI());
        String prevPage = (String) session.getAttribute("prevPage");


        System.out.println("CustomLoginSuccessHandler's onAuthenticationSuccess! ");
        Collection<? extends GrantedAuthority> collection = authentication.getAuthorities();

        collection.forEach((role) -> {
            try {
                System.out.println("role : " + role.getAuthority());
                String role_str = role.getAuthority();

                if (role_str.equals("ROLE_USER")) {

                    System.out.println("USER 페이지로 이동!");
                    response.sendRedirect(prevPage);
                    return ;
                } else if (role_str.equals("ROLE_MEMBER")) {
                    System.out.println("MEMBER 페이지로 이동!");
                    response.sendRedirect(request.getHeader("referer"));
                    return ;
                } else if (role_str.equals("ROLE_ADMIN")) {
                    System.out.println("ADMIN 페이지로 이동!");
                    response.sendRedirect(request.getHeader("referer"));
                    return ;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}
