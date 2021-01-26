package com.eventoApp.models;

import java.util.Collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class User {
	
	private String userName;

	private String password;

	private String matchingPassword;

	private String firstName;

	private String lastName;

	private String email;
	
	private Collection<Role> roles;
}
