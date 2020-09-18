package com.eventoApp.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Role implements GrantedAuthority, Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("roleName")
	private String roleName;
	
	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private List<User> users;

	@Override
	public String getAuthority() {
		return this.roleName;
	}
}
