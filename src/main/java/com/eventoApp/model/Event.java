package com.eventoApp.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Event implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("codigo")
	private long codigo;
	
	@JsonProperty("nome")
	private String nome;
	
	@JsonProperty("local")
	private String local;
	
	@JsonProperty("data")
	private String data;
	
	@JsonProperty("horario")
	private String horario;
	
	private List<Guest> convidados;


	public long getCodigo() {
		return codigo;
	}
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}

}
