package com.eventoApp.configs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	@Autowired
	private SessionRegistry session;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		session.getAllSessions(authentication.getPrincipal(), false)
		.stream()
		.forEach(s -> session.removeSessionInformation(s.getSessionId()));
				
		String URL = "http://localhost:4200/home";
		response.setStatus(HttpStatus.OK.value());
		response.sendRedirect(URL);
	}
}
