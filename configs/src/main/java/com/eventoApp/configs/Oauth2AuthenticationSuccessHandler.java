package com.eventoApp.configs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.eventoApp.models.User;
import com.eventoApp.services.ClientService;

import reactor.core.publisher.Mono;

@Component("oauth2authSuccessHandler")
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private Logger log = LoggerFactory.getLogger(Oauth2AuthenticationSuccessHandler.class);
	
	@Autowired
	private ClientService sr;

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
			throws IOException, ServletException {
		
		String userName = getLoggedUserName(authentication).block();
		log.info("Username: " + userName);
		
		sr.saveSession(User.builder().userName(userName).build());
	}
	
	
	public Mono<String> getLoggedUserName(Authentication authentication) {
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			
			org.springframework.security.core.userdetails.User userNamePrincipal = (org.springframework.security.core.userdetails.User) ((UsernamePasswordAuthenticationToken) authentication).getPrincipal();
		    attributes.put("username", userNamePrincipal.getUsername());
			
		} else if (authentication instanceof OAuth2AuthenticationToken) {
			
			attributes = ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttributes();
			
		} else {
			
			attributes.put("username", "Visitor");
		}
		
		Mono<String> user = Mono.just(attributes.get("username").toString());
		return user;
	}
}
