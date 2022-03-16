package com.eventoApp.configs;

import java.io.IOException;
import static java.util.Objects.nonNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	@Autowired
	@Qualifier("sessionsPersisted")
	private SessionRegistry session;

	@Value(value = "${EVENTOANGULAR_HOST}")
	private String eventoAngularHost;


	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		session.getAllSessions(nonNull(authentication) ? authentication.getPrincipal() : new Object() , false)
				.stream()
				.forEach(s -> session.removeSessionInformation(s.getSessionId()));
				
		String URL = eventoAngularHost + "/home";
		response.setStatus(HttpStatus.OK.value());
		response.sendRedirect(URL);
	}
}
