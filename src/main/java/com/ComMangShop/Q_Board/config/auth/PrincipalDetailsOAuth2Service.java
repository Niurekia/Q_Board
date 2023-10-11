package com.ComMangShop.Q_Board.config.auth;


import com.ComMangShop.Q_Board.config.auth.provider.GoogleUserInfo;
import com.ComMangShop.Q_Board.config.auth.provider.KakaoUserInfo;
import com.ComMangShop.Q_Board.config.auth.provider.NaverUserInfo;
import com.ComMangShop.Q_Board.config.auth.provider.OAuth2UserInfo;
import com.ComMangShop.Q_Board.domain.dto.UserDto;
import com.ComMangShop.Q_Board.domain.entity.User;
import com.ComMangShop.Q_Board.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PrincipalDetailsOAuth2Service extends DefaultOAuth2UserService   implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    public PrincipalDetailsOAuth2Service(){
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

//        System.out.println("[OAUTH2 Service] loadUser CALL");
//        System.out.println("userRequest : "+ userRequest);
//        System.out.println("userRequest.getClientRegistration() : "+ userRequest.getClientRegistration());
//        System.out.println("userRequest.getAccessToken() : "+ userRequest.getAccessToken());
//        System.out.println("userRequest.getAdditionalParameters() : "+ userRequest.getAdditionalParameters());
//
//        System.out.println("getAccessToken().getTokenValue() : "+ userRequest.getAccessToken().getTokenValue());
//        System.out.println("getAccessToken().getTokenType() : "+ userRequest.getAccessToken().getTokenType().getValue());
//        System.out.println("getAccessToken().getScopes() : "+ userRequest.getAccessToken().getScopes());



//      System.out.println("super.loadUser : " +super.loadUser(userRequest));
        OAuth2User oauth2User =   super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;
        System.out.println("auth2User.getAttributes() : " +oauth2User.getAttributes());
        if(userRequest.getClientRegistration().getRegistrationId().equals("kakao"))
        {
            System.out.println("[] 카카오 로그인");
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo((Map<String, Object>) oauth2User.getAttributes().get("properties"));
            kakaoUserInfo.setId(userRequest.getClientRegistration().getClientId());
            oAuth2UserInfo = kakaoUserInfo;
            //oAuth2UserInfo = new KakaoUserInfo((Map<String, Object>) oauth2User.getAttributes().get("properties"));
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("google"))
        {
            System.out.println("[] 구글 로그인");
            oAuth2UserInfo  = new GoogleUserInfo(oauth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver"))
        {
            System.out.println("[] 네이버 로그인");
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oauth2User.getAttributes().get("response"));
        }

        //OAuth2UserInfo 확인
        String provider  = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+"_"+providerId+"@example.com";    //  /
        String password = passwordEncoder.encode("1234");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        //DB 저장
        Optional<User> optional =   userRepository.findById(username);
        if(optional.isEmpty()) {
            User user = User.builder()
                    .username(username)
                    .password(password)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
            System.out.println("[OAUTH] "+provider +" 최초 로그인 요청!");
        }else{
            System.out.println("[OAUTH] 기존 계정"+optional.get().getUsername() +"으로 로그인!");
        }


        //AccessToken 정보를 Authentication에 저장하기
        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setAttributes(oauth2User.getAttributes());
        UserDto dto = new UserDto();
        dto.setUsername(username);
        dto.setPassword(password);
        dto.setRole(role);
        //OAUTH2 LOGOUT
        dto.setProvider(provider);
        dto.setProviderId(providerId);
        principalDetails.setUser(dto);
        principalDetails.setAccessToken(userRequest.getAccessToken().getTokenValue());

        return principalDetails;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user =  userRepository.findById(username);

        if(user.isEmpty())
            return null;

        UserDto dto = new UserDto();
        dto.setUsername(user.get().getUsername());
        dto.setPassword(user.get().getPassword());
        dto.setRole(user.get().getRole());
        dto.setPhone(user.get().getPhone());
        dto.setZipcode(user.get().getZipcode());
        dto.setAddr1(user.get().getAddr1());
        dto.setAddr2(user.get().getAddr2());

        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setUser(dto);

        return principalDetails;
    }
}
