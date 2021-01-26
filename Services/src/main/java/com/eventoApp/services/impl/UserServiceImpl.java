package com.eventoApp.services.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.eventoApp.models.CrmUser;
import com.eventoApp.models.Role;
import com.eventoApp.models.User;
import com.eventoApp.services.ClientService;
import com.eventoApp.services.UserService;

public class UserServiceImpl implements UserService {
	
	@Autowired
	private ClientService clientService;


	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public User findByUserName(String login) {
		
		return clientService.seekUser(login);
	}

	@Override
	public void save(CrmUser crmUser) {
		User user = new User();
		 // assign user details to the user object
		user.setUserName(crmUser.getUserName());
		user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
		user.setFirstName(crmUser.getFirstName());
		user.setLastName(crmUser.getLastName());
		user.setEmail(crmUser.getEmail());
		
		if ((crmUser.getFormRole() != null) & !(crmUser.getFormRole().equals("USER"))) {
			user.setRoles(Arrays.asList(clientService.seekRoleByName("USER"), clientService.seekRoleByName(crmUser.getFormRole())));
		} else {
			// give user default role of "employee"
			user.setRoles(Arrays.asList(clientService.seekRoleByName("USER")));
		}
		 // save user in the database
		clientService.saveUser(user);
	}
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = clientService.seekUser(userName);
		
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}
