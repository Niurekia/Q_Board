package com.ComMangShop.Q_Board.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@Slf4j

public class HomeController {

    @GetMapping("/")
    public String home(){
        log.info("/GET /");
        return "templates";
    }


    @GetMapping("/login")
    public String login_get(Model model, @RequestHeader(value = "referer", required = false) String referrer) {
        if (referrer != null) {
            model.addAttribute("prevPage", referrer);
        } else {
            model.addAttribute("prevPage", "/"); // 기본 페이지 URL 또는 다른 URL로 대체
        }

        return "login"; // login.html Thymeleaf 템플릿을 사용
    }
    @GetMapping("/logout")
    public void logout_get()
    {

    }


    @GetMapping("/templates")
    public void templates(){
        log.info("/GET /templates");
    }

}
