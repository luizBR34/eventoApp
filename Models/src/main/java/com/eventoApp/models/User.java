package com.eventoApp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("username")
	private String username;
	
	@JsonProperty("fullName")
	private String fullName;
	
	@JsonProperty("password")
	private String password;

	private Role role;
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		ArrayList<Role> roles = new ArrayList<Role>();
		roles.add(this.role);

		return roles;
	}


	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
