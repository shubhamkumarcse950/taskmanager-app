package com.taskManagement.ServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taskManagement.Entitys.User;
import com.taskManagement.Repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtUserDetails implements UserDetailsService {
	private final UserRepo userRepo;

	public JwtUserDetails(UserRepo userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			User userDetails = userRepo.findByEmail(email);
			if (userDetails == null) {
				throw new IllegalArgumentException("Invalid username or password.");
			}
			Set<SimpleGrantedAuthority> authorities = getAuthority(userDetails.getRoles());
			return new org.springframework.security.core.userdetails.User(userDetails.getEmail(),
					userDetails.getPassword(), true, true, true, true, authorities);
		} catch (UsernameNotFoundException e) {
			log.error("user name not found:{}", e.getMessage());
			throw new IllegalArgumentException("Invalid username or password.");
		}

	}

	public Set<SimpleGrantedAuthority> getAuthority(List<String> roles) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}
