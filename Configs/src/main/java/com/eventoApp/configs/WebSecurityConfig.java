package com.eventoApp.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.eventoApp.services.UserService;


@Configuration
@EnableWebSecurity
@Order(1)
@EnableOAuth2Client
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	@Qualifier("oauth2authSuccessHandler")
	private AuthenticationSuccessHandler oauth2authSuccessHandler;



	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.authorizeRequests()
		.antMatchers("/", "index", "/login", "/showMyLoginPage", "/logout", "/access-denied", "/h2-console/**", "/verify/**").permitAll()
/*		.antMatchers(HttpMethod.GET, "/cadastrarEvento").hasRole("ADMIN") 
		.antMatchers(HttpMethod.POST, "/cadastrarEvento").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST, "/**").hasAnyRole("ADMIN", "USER")
		.antMatchers(HttpMethod.GET, "/deletarEvento").hasRole("ADMIN")
		.antMatchers(HttpMethod.GET, "/deletarConvidado").hasAnyRole("ADMIN", "USER")*/
		.anyRequest().authenticated()

		.and()
		.formLogin()
				.loginPage("/login")
				//.loginProcessingUrl("/authenticateUser")
				.successHandler(oauth2authSuccessHandler)
				//.failureUrl("/login?error=true")
				//.permitAll()
				
		//NOvo		
		.and()
				.csrf().disable().rememberMe().key("myremembermekey")	
		.and()
		.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/").deleteCookies("remember-me")  // after logout redirect to landing page (root)
				//.permitAll()
		.and()
				.exceptionHandling()
				.accessDeniedPage("/access-denied")
				
		.and()
		.oauth2Login()
			.loginPage("/login")
			.successHandler(oauth2authSuccessHandler);

			//.authorizationEndpoint()
			//.baseUri("/userlogin/oauth2/authorization");

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
		auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
		return auth;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		DelegatingPasswordEncoder encoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return encoder;
	}
	
}
