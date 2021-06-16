package com.eventoApp.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.eventoApp.services.UserService;


@Configuration
@EnableWebSecurity
@Order(1)
@EnableOAuth2Client
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	@Qualifier("oauth2authSuccessHandler")
	private AuthenticationSuccessHandler oauth2authSuccessHandler;
	
	
	@Autowired
	private BCryptPasswordEncoder encoder;


	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.authorizeRequests().antMatchers(HttpMethod.GET, "/").anonymous()
        .antMatchers(HttpMethod.GET, "/events/*", "/loggedUser/*", "/oauth2/authorization/**", "/logout", "/access-denied", "/h2-console/**").permitAll()
        .antMatchers(HttpMethod.POST, "/logar/*").permitAll()
        .anyRequest().authenticated()
		.and()
			.formLogin()
				.loginPage("http://localhost:4200/home?login=true")
				.loginProcessingUrl("/logar/*")
				.successHandler(oauth2authSuccessHandler)
				.failureUrl("http://localhost:4200/home?login=false")
	
        .and()
		.oauth2Login()
			.loginPage("http://localhost:4200/home?login=true")
			.successHandler(oauth2authSuccessHandler)
			
		.and()
			.rememberMe().key("myremembermekey")	
		.and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("http://localhost:4200/home").deleteCookies("remember-me")
				
		.and()
			.exceptionHandling()
			.accessDeniedPage("/access-denied");
		
		http.headers().frameOptions().disable();
	}
	
	
	//Define a autenticação de páginas estáticas (não bloquear style)
	@Override
	public void configure(WebSecurity web) throws java.lang.Exception {
		web.ignoring().antMatchers("/materialize/**", "/style/**", "/images/**");
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws java.lang.Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	

	//authenticationProvider bean definition
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService); //set the custom user details service
		auth.setPasswordEncoder(encoder); //set the password encoder - bcrypt
		return auth;
	}
}
