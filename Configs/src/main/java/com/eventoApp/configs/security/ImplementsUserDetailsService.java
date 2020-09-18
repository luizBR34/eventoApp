package com.eventoApp.configs.security;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.eventoApp.service.ClientService;

@Repository
@Transactional
public class ImplementsUserDetailsService implements UserDetailsService {
	
	private Logger log = LoggerFactory.getLogger(ImplementsUserDetailsService.class);
	
	@Autowired
	private ClientService service;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		
		com.eventoApp.model.User usuario = service.buscaUsuario(login);
		
		if (usuario == null) {
			log.warn("ImplementsUserDetailsService:loadUserByUsername() - USUARIO NÃO ENCONTRADO!");
			throw new UsernameNotFoundException("Usuario não encontrado!");
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, usuario.getAuthorities());
	}
}
