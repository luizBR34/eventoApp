package com.eventoApp.services;

import java.util.List;

import com.eventoApp.models.Guest;
import com.eventoApp.models.Role;
import com.eventoApp.models.Event;
import com.eventoApp.models.User;

public interface ClientService {
	
	public List<Event> listEvents();
	
	public Event seekEvent(long code);

	public User seekUser(String login);
	
	public void saveUser(User user);
	
	public Role seekRoleByName(String theRoleName);
	
	public List<Guest> listGuests(long eventCode);
	
	public void saveGuest(long eventCode, Guest guest);
	
	public void saveEvent(Event evento);
	
	public void deleteEvent(long code);
	
	public Event deleteGuest(long id);
}
