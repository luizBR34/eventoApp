package com.eventoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * PRA FUNCIONAR PRECISA ANTES REALIZAR UM INSERT NA TABELA USUARIO COM UM USUARIO
 * INSERT INTO Usuario(login, nomeCompleto, senha) VALUES('luizpovoa', 'Luiz Fernando Povoa', '12345')
 */

@SpringBootApplication
public class EventoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventoAppApplication.class, args);
	}
}
