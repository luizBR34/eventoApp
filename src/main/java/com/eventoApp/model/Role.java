package com.eventoApp.model;

import java.util.List;

import javax.persistence.Column;

/*
 * INSERT INTO PAPEL(nomeRule) VALUES('RULE_ADMIN')
 * INSERT INTO PAPEL(nomeRule) VALUES('RULE_USER')
 * INSERT INTO usuarios_rules(usuario_id, rule_id) VALUES('luizpovoa', 'RULE_ADMIN')
 */


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "Role")
public class Role implements GrantedAuthority {


	@Id
	@Column(name = "nomeRole")
	private String nomeRole;
	
	@ManyToMany(mappedBy = "roles")
	private List<Usuario> usuarios;
	

	public String getNomeRole() {
		return nomeRole;
	}


	public void setNomeRole(String nomeRole) {
		this.nomeRole = nomeRole;
	}


	public List<Usuario> getUsuarios() {
		return usuarios;
	}


	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}


	@Override
	public String getAuthority() {
		return this.nomeRole;
	}
}
