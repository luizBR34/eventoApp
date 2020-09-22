package com.eventoApp.services;

import java.util.List;

import com.eventoApp.models.Guest;
import com.eventoApp.models.Event;
import com.eventoApp.models.User;

public interface ClientService {
	
	public List<Event> listEvents();
	
	public Event searchEvent(long code);

	public User seekUser(String login);
	
	public List<Guest> listGuests(Event event);
	
	//public boolean cadastraConvidado(long codigoEvento, Convidado convidado);
	
	public void saveEvent(Event evento);
	
	//public void deletaEvento(long codigo);
	
	//public Evento deletaConvidado(String rg);
}
