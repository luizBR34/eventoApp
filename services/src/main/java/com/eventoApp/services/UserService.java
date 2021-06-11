package com.eventoApp.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventoApp.models.CrmUser;
import com.eventoApp.models.User;

public interface UserService extends UserDetailsService {
	
	User findByUserName(String login);
	
	void save(CrmUser crmUser);
}
