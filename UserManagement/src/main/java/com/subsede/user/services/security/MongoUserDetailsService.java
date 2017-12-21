package com.subsede.user.services.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextListener;

import com.subsede.user.model.user.Role;
import com.subsede.user.repository.user.UserRepository;

@Component
public class MongoUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository<com.subsede.user.model.user.User> userRepository;

	@Autowired
	private LoginAttemptService loginAttemptService;
	
    /*@Autowired
    private HttpServletRequest request;*/

    @Bean public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    } 
    
	private org.springframework.security.core.userdetails.User userdetails;

	public UserDetails loadUserByUsername(String username) {

//		String ip = getClientIP();
		com.subsede.user.model.user.User user = getUserDetail(username);
		if (loginAttemptService.isBlocked(username)) {
			if(user != null) {
				user.setAccountLocked(true);
				userRepository.save(user);
				loginAttemptService.removeUserFromCache(username);
			}
			throw new LockedException("User is blocked");
		}
		
		if(user == null)
			throw new BadCredentialsException("Bad credentials");

		System.out.println(username);
		System.out.println(user.getPassword());
		System.out.println(user.getUsername());
		System.out.println(user.getRoles());

		userdetails = new User(user.getUsername(), user.getPassword(), user.isEnabled(), !user.isAccountExpired(),
				!user.isPasswordExpired(), !user.isAccountLocked(), getAuthorities(user.getRoles()));
		return userdetails;
	}

	public List<GrantedAuthority> getAuthorities(Set<Role> roles) {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		for (Role role : roles) {
			authList.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		}
		return authList;
	}

	public com.subsede.user.model.user.User getUserDetail(String username) {
		com.subsede.user.model.user.User user = userRepository.findByUsername(username);
		return user;
	}

	/*private String getClientIP() {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}*/

}