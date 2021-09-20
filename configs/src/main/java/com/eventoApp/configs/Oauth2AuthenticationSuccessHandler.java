package com.eventoApp.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component("oauth2authSuccessHandler")
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private Logger log = LoggerFactory.getLogger(Oauth2AuthenticationSuccessHandler.class);

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
			throws IOException, ServletException {

		Map<String, Object> attributes = getAttributesFromAuthenticatedUser(authentication);
		String userName = attributes.containsKey("username") ? attributes.get("username").toString() : attributes.get("given_name").toString();
		String token = attributes.containsKey("tokenValue") ? attributes.get("tokenValue").toString() : "";

		log.info("Username: " + userName);

		redirectStrategy.sendRedirect(request, response, "http://localhost:4200/eventos?username=" + userName + "&token=" + token);
	}
	
	
	public Map<String, Object> getAttributesFromAuthenticatedUser(Authentication authentication) {
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			
			org.springframework.security.core.userdetails.User userNamePrincipal = (org.springframework.security.core.userdetails.User) ((UsernamePasswordAuthenticationToken) authentication).getPrincipal();
		    attributes.put("username", userNamePrincipal.getUsername());
			
		} else if (authentication instanceof OAuth2AuthenticationToken) {
			
			attributes = ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttributes();
			
		} else {
			
			attributes.put("username", "Visitor");
		}

		return attributes;
	}
}
