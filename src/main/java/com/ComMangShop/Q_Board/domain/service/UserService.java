package com.ComMangShop.Q_Board.domain.service;


import com.ComMangShop.Q_Board.domain.dto.UserDto;
import com.ComMangShop.Q_Board.domain.entity.User;
import com.ComMangShop.Q_Board.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean joinMember(UserDto dto, Model model, HttpServletRequest request)
    {

        //패스워드 일치 여부 확인
        if(!dto.getPassword().equals(dto.getRepassword()))
        {
            model.addAttribute("repassword","패스워드가 일치하지 않습니다");
            return false;
        }

        //Email 인증 여부 확인
        HttpSession session=request.getSession();
        Boolean is_email_auth=(Boolean)session.getAttribute("is_email_auth");
        if(is_email_auth!=null){
            if(is_email_auth)//코드인증 확인
            {
                dto.setRole("ROLE_USER");
                dto.setPassword(passwordEncoder.encode(dto.getPassword()));

                User user=UserDto.dtoToEntity(dto);

                userRepository.save(user);

                return true;
            }
            else    //인증 실패
            {
                return false;
            }
        }
        else {
            return false;
        }


    }

    public void updateProfile(UserDto dto) {
        User user = UserDto.dtoToEntity(dto);

        userRepository.save(user);
    }

}
