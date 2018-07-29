package com.eventoApp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "Convidado")
public class Convidado implements Serializable {
	
	private static final long serialVersionUID = 108L;

	@Id
	@NotEmpty
	@Column(name = "rg")
	private String rg;
	
	@NotEmpty
	@Column(name = "nomeConvidado")
	private String nomeConvidado;
	
	//Aqui se especifica a relação entre tabelas
	//Muitos Convidados para um Evento só
	@ManyToOne
	private Evento evento;
	
	
	protected Convidado() {}
	
	
	public String getRg() {
		return rg;
	}
	public void setRg(String rg) {
		this.rg = rg;
	}
	public String getNomeConvidado() {
		return nomeConvidado;
	}
	public void setNomeConvidado(String nomeConvidado) {
		this.nomeConvidado = nomeConvidado;
	}
	
	
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}

}
