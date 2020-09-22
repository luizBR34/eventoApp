package com.eventoApp.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class EventoAppApplication extends SpringBootServletInitializer {
	
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EventoAppApplication.class);
    }
 
    public static void main(String[] args) {
    	//System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(EventoAppApplication.class, args);
    }

}