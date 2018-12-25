package com.eventoApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementsUserDetailsService userDetailsService;

	//Define como é feita a autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws java.lang.Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}
	
	
	//Define quais páginas vão precisar de autenticação
	@Override
	protected void configure(HttpSecurity http) throws java.lang.Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.GET, "/").permitAll() // Permite apenas a requisição que termina em / não ter autenticação
		.antMatchers(HttpMethod.GET, "/cadastrarEvento").hasRole("ADMIN") //Apenas admin tem acesso a essa operação
		.antMatchers(HttpMethod.POST, "/cadastrarEvento").hasRole("ADMIN")
		.antMatchers("/login*").permitAll()
		.and().formLogin().loginPage("/login").defaultSuccessUrl("/", true).failureUrl("/login?error=true")
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")); //Define uma view de logout
	}
	
	
	//Define a autenticação de páginas estáticas (não bloquear style)
	@Override
	public void configure(WebSecurity web) throws java.lang.Exception {
		web.ignoring().antMatchers("/materialize/**", "/style/**");
	}

}
