package com.eventoApp.service;

import java.util.List;

import com.eventoApp.model.Guest;
import com.eventoApp.model.Event;
import com.eventoApp.model.User;

public interface ClientService {
	
	public List<Event> listEvents();

	
	//public Evento buscaEvento(long codigo);

	//public Usuario buscaUsuario(String login);
	
	//public Iterable<Convidado> listaConvidados(Evento evento);
	
	//public boolean cadastraConvidado(long codigoEvento, Convidado convidado);
	
	public void saveEvent(Event evento);
	
	//public void deletaEvento(long codigo);
	
	//public Evento deletaConvidado(String rg);
}
