package com.eventoApp.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Guest implements Serializable {
	
	private static final long serialVersionUID = 1L;


	@JsonProperty("rg")
	private String rg;
	
	@JsonProperty("nomeConvidado")
	private String nomeConvidado;
	
	private Event evento;

	
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
	
	public Event getEvento() {
		return evento;
	}
	public void setEvento(Event evento) {
		this.evento = evento;
	}
}
