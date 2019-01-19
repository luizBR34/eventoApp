package com.eventoApp.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Convidado implements Serializable {
	
	private static final long serialVersionUID = 1L;


	@JsonProperty("rg")
	private String rg;
	
	@JsonProperty("nomeConvidado")
	private String nomeConvidado;
	
	private Evento evento;

	
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
