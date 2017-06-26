package com.company.eventListeners;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String errorMessage = "Bad credentials";
		setDefaultFailureUrl("/login?error");
		if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
			errorMessage = "Invalid credentials";
		}

		else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
			errorMessage = "User disabled";
		}

		else if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
			errorMessage = "Invalid credentials";
		}

		else if (exception.getClass().isAssignableFrom(LockedException.class)) {
			errorMessage = "User blocked";
		}
		request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
		super.onAuthenticationFailure(request, response, exception);
	}

}