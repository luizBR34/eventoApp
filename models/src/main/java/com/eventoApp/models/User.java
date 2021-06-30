package com.eventoApp.models;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User implements Serializable {

	private static final long serialVersionUID = -203264994057523497L;

	private Long id;

	private String userName;

	private String password;

	private String firstName;

	private String lastName;

	private String email;

	private Collection<Role> roles;
	
	//private Collection<Event> events;
}
