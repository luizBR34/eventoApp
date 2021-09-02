package com.eventoApp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	

	@Bean
	public WebClient webClient(ClientRegistrationRepository clientRep, OAuth2AuthorizedClientRepository authClientRepo) {
		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
				
				new ServletOAuth2AuthorizedClientExchangeFilterFunction(
						clientRep, 
						authClientRepo
						);
		oauth2.setDefaultClientRegistrationId("eventoas");
		return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
	}


	@Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
	
	
		
