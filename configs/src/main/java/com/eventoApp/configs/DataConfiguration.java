package com.eventoApp.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Collections;
import java.util.Locale;

@Configuration
public class DataConfiguration implements WebMvcConfigurer {

	@Value(value = "${EVENTOANGULAR_HOST}")
	private String eventoAngularHost;

	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setDefaultLocale(Locale.US);
	    return slr;
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("lang");
	    return lci;
	}
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowedOrigins(Collections.singletonList(eventoAngularHost));
		config.setAllowedMethods(Collections.singletonList("*"));
	    config.setAllowedHeaders(Collections.singletonList("*"));
		config.setAllowCredentials(true);
	    
	    source.registerCorsConfiguration("/**", config);  
	    
	    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
	    bean.setFilter(new CorsFilter(source));
	    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);  
	    
	    return bean;  
	}
}
