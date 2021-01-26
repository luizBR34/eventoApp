package com.eventoApp.configs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.eventoApp.models.CrmUser;
import com.eventoApp.services.UserService;

@Component("oauth2authSuccessHandler")
public class Oauth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	private  RedirectStrategy redirectStrategy =  new DefaultRedirectStrategy();

	@Autowired
	private UserService service;
	
	@Autowired
	private  PasswordEncoder encoder;
	 
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2AuthenticationToken oToken = (OAuth2AuthenticationToken) authentication;
		
		com.eventoApp.models.User userFound = service.findByUserName(authentication.getName());
		
		if(userFound == null) {
			
			String firstName = oToken.getPrincipal().getAttributes().get("given_name").toString();
			String lastName = oToken.getPrincipal().getAttributes().get("family_name").toString();
			String email = oToken.getPrincipal().getAttributes().get("email").toString();
			
			CrmUser user = new CrmUser();
			user.setUserName("admin");
			user.setPassword(encoder.encode("secret"));
			user.setMatchingPassword(encoder.encode("secret"));
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);

			service.save(user);
			
		} else {

			HttpSession session = request.getSession();
			session.setAttribute("user", userFound);
		}

		this.redirectStrategy.sendRedirect(request, response, "/hello");
		
		// forward to home page
		//response.sendRedirect(request.getContextPath() + "/");
	}
}
