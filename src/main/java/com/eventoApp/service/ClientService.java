package com.eventoApp.service;

import com.eventoApp.model.Convidado;
import com.eventoApp.model.Evento;
import com.eventoApp.model.Usuario;

public interface ClientService {
	
	public Iterable<Evento> listaEventos();

	public Evento buscaEvento(long codigo);

	public Usuario buscaUsuario(String login);
	
	public Iterable<Convidado> listaConvidados(Evento evento);
	
	public boolean cadastraConvidado(long codigoEvento, Convidado convidado);
	
	public boolean cadastraEvento(Evento evento);
	
	public void deletaEvento(long codigo);
	
	public Evento deletaConvidado(String rg);
}
