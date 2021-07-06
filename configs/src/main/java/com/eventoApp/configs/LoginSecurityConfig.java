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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
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
	
	@Autowired
	private CustomLogoutSuccessHandler logoutSuccessHandler;


	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().sessionManagement().maximumSessions(1).sessionRegistry(registroSecao())
		.and()
			.sessionCreationPolicy(SessionCreationPolicy.NEVER)
		.and()
		.authorizeRequests().antMatchers(HttpMethod.GET, "/").anonymous()
        .antMatchers(HttpMethod.GET, "/events/*", "/loggedUser/*", "/oauth2/authorization/**", "/logout", "/access-denied", "/h2-console/**").permitAll()
        .antMatchers(HttpMethod.POST, "/logar/*", "/saveEvent/").permitAll()
        .anyRequest().authenticated()
		.and()
			.formLogin()
				.loginPage("http://localhost:4200/home?login=true")
				.loginProcessingUrl("/logar/*")
				.successHandler(oauth2authSuccessHandler)
	
        .and()
		.oauth2Login()
			.loginPage("http://localhost:4200/home?login=true")
			.successHandler(oauth2authSuccessHandler)
			
		.and()
			.rememberMe().key("remember-me")	
		.and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(logoutSuccessHandler)
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
		.and()
			.exceptionHandling()
			.accessDeniedPage("/access-denied");
		
		http.headers().frameOptions().disable();
	}
	
	
    @Bean
    public SessionRegistry registroSecao() {
        return new SessionRegistryImpl();
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
