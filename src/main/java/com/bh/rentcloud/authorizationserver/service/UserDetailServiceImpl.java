package com.bh.rentcloud.authorizationserver.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bh.rentcloud.authorizationserver.model.AuthUserDetail;
import com.bh.rentcloud.authorizationserver.model.User;
import com.bh.rentcloud.authorizationserver.repository.UserDetailRepository;

@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		PasswordEncoder encoder= new BCryptPasswordEncoder();
		Optional<User> optionalUser = null;
		
		
		try {
			optionalUser = userDetailRepository.findByUsername(name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username or password wrong"));
			User user = optionalUser.get();
		UserDetails userDetails = new AuthUserDetail(optionalUser.get());
//		new AccountStatusUserDetailsChecker().check(userDetails);
		
		
		System.out.println("==================================> PWD  : "+ encoder.matches("SANKAR@123",user.getPassword()));
		System.out.println("==================================> PWD  : "+ user.getPassword());
		System.out.println("==================================>  ENCO : "+ passwordEncoder.encode("SANKAR@123"));
		return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),getGrantedAuthorities(user));

	}
	
	private Collection<GrantedAuthority> getGrantedAuthorities(User user){
	    Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();
	        grantedAuthority.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	    grantedAuthority.add(new SimpleGrantedAuthority("ROLE_USER"));
	    return grantedAuthority;
	}
}
