package com.eventoApp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventoApp.clients.EventoAppFeignClient;
import com.eventoApp.models.Event;
import com.eventoApp.models.Guest;
import com.eventoApp.models.Role;
import com.eventoApp.models.User;
import com.eventoApp.services.ClientService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultClientService implements ClientService {

	@Value(value = "${eventocache.endpoint.uri}")
	private String eventoCacheEndpointURI;

	@Autowired
	private EventoAppFeignClient client;

	@Override
	public List<Event> eventList(String username) {

		log.info("START - DefaultClientService:eventList()");

		ResponseEntity<List<Event>> responseEntity = client.eventList(username);
		List<Event> listOfEvents = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:listEvents() - EventoCache API responded the request successfully!");
			listOfEvents = responseEntity.getBody();
		} else {
			log.error("Error when request event's list from API!");
		}

		log.info("END - DefaultClientService:eventList()");
		return listOfEvents;
	}

	@Override
	public Event seekEvent(String username, long code) {

		log.info("START - DefaultClientService:searchEvent()");

		ResponseEntity<Event> responseEntity = client.seekEvent(username, code);
		Event event = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:searchEvent() - EventoCache API responded the request successfully!");
			event = responseEntity.getBody();
		} else {
			log.error("Error when seek for an event!");
		}

		log.info("END - DefaultClientService:searchEvent()");
		return event;
	}

	@Override
	public User seekUser(String login) {

		log.info("START - DefaultClientService:seekUser()");

		ResponseEntity<User> responseEntity = client.seekUser(login);
		User user = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:seekUser() - EventoCache API responded the request successfully!");
			user = responseEntity.getBody();
		} else {
			log.error("Error when to seek for an User!");
		}

		log.info("END - DefaultClientService:seekUser()");
		return user;
	}
	
	
	@Override
	public void saveUser(User user) {
		

	}
	

	@Override
	public List<Guest> guestList(long eventCode) {

		log.info("START - DefaultClientService:listGuests()");

		ResponseEntity<List<Guest>> responseEntity = client.guestList(eventCode);
		List<Guest> guestList = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:listGuests() - EventoCache API responded the request successfully!");
			guestList = responseEntity.getBody();
		} else {
			log.error("Error when request guest's list from API!");
		}

		log.info("END - DefaultClientService:listGuests()");
		return guestList;
	}

	@Override
	public void saveGuest(long eventCode, Guest guest) {
		
		log.info("START - DefaultClientService:saveGuest()");
		
		ResponseEntity<Void> responseEntity = client.saveGuest(eventCode, guest);

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:saveGuest() - The Guest was successfully saved!");
		} else {
			log.error("Error when to save the Guest!");
		}
		
		log.info("END - DefaultClientService:saveGuest()");
	}

	@Override
	public void saveEvent(Event event) {
		
		log.info("START - DefaultClientService:saveEvent()");
		
		ResponseEntity<Void> responseEntity = client.saveEvent(event);

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:saveEvent() - The Event was successfully saved!");
		} else {
			log.error("Error when to save the session!");
		}
		
		log.info("END - DefaultClientService:saveEvent()");
	}

	@Override
	public void deleteEvent(String username, long code) {

		log.info("START - DefaultClientService:deleteEvent()");
		
		client.deleteEvent(username, code);

		log.info("END - DefaultClientService:deleteEvent()");
	}

	public Event deleteGuest(long id) {

		log.info("START - DefaultClientService:deleteGuest()");

		ResponseEntity<Event> responseEntity = client.deleteGuest(id);
		Event event = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:deleteGuest) - EventoWS API responded the request successfully!");
			event = responseEntity.getBody();
		} else {
			log.error("Error when to delete one guest!");
		}

		log.info("END - DefaultClientService:deleteGuest()");
		return event;
	}

	@Override
	public Role seekRoleByName(String theRoleName) {

		log.info("START - DefaultClientService:seekRoleByName()");

		ResponseEntity<Role> responseEntity = client.seekRoleByName(theRoleName);
		Role role = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:seekRoleByName() - EventoCache API responded the request successfully!");
			role = responseEntity.getBody();
		} else {
			log.error("Error when seek a Role by their name!");
		}

		log.info("END - DefaultClientService:seekRoleByName()");
		return role;
	}

	@Override
	public void saveSession(User user) {

		log.info("START - DefaultClientService:saveSession()");

		ResponseEntity<Void> responseEntity = client.saveSession(user);

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:saveSession() - EventoCache API responded the request successfully!");
		} else {
			log.error("Error when to save the session!");
		}

		log.info("END - DefaultClientService:saveSession()");
	}

	@Override
	public User getSession() {

		log.info("START - DefaultClientService:getSession()");

		ResponseEntity<User> responseEntity = client.getSession();
		User user = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:getSession() - EventoCache API responded the request successfully!");
			user = responseEntity.getBody();
		} else {
			log.error("Error when request the session!");
		}

		log.info("END - DefaultClientService:getSession()");
		return user;
	}
}
