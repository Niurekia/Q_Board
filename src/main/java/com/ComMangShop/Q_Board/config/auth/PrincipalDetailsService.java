package com.ComMangShop.Q_Board.config.auth;


import com.ComMangShop.Q_Board.domain.dto.UserDto;
import com.ComMangShop.Q_Board.domain.entity.User;
import com.ComMangShop.Q_Board.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> user =  userRepository.findById(username);

		if(user.isEmpty())
			return null;

		UserDto dto = new UserDto();
		dto.setUsername(user.get().getUsername());
		dto.setPassword(user.get().getPassword());
		dto.setRole(user.get().getRole());

		PrincipalDetails principalDetails = new PrincipalDetails();
		principalDetails.setUser(dto);

		return principalDetails;

	}


}
