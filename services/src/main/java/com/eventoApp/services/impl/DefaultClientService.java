package com.eventoApp.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventoApp.clients.EventoAppFeignClient;
import com.eventoApp.models.Event;
import com.eventoApp.models.Guest;
import com.eventoApp.models.Role;
import com.eventoApp.models.User;
import com.eventoApp.services.ClientService;

@Service
public class DefaultClientService implements ClientService {

	private Logger log = LoggerFactory.getLogger(DefaultClientService.class);

	@Autowired
	@Qualifier("template")
	private AmqpTemplate template;

	@Autowired
	private Environment env;

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
	public Event seekEvent(long code) {

		log.info("START - DefaultClientService:searchEvent()");

		ResponseEntity<Event> responseEntity = client.seekEvent(code);
		Event event = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("DefaultClientService:searchEvent() - EventoCache API responded the request successfully!");
			event = responseEntity.getBody();
		} else {
			log.error("Error when request event's list from API!");
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
			log.error("Error when request event's list from API!");
		}

		log.info("END - DefaultClientService:seekUser()");
		return user;
	}
	
	
	@Override
	public void saveUser(User user) {
		
		log.info("START - DefaultClientService:saveUser()");

		String topicExchangeBroker = env.getProperty("name.topicexchange.eventoapp");
		String routingKey = env.getProperty("name.routingKey.eventoapp");

		template.convertAndSend(topicExchangeBroker, routingKey, user);

		log.info("END - DefaultClientService:saveUser()");
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
			log.error("Error when request event's list from API!");
		}

		log.info("END - DefaultClientService:listGuests()");
		return guestList;
	}

	@Override
	public void saveGuest(long eventCode, Guest guest) {

		log.info("START - DefaultClientService:saveGuest()");

		String topicExchangePrice = env.getProperty("name.topicexchange.assortment");
		String routingKey = env.getProperty("name.routingKey.updates");

		template.convertAndSend(topicExchangePrice, routingKey, guest);

		log.info("END - DefaultClientService:saveGuest()");
	}

	@Override
	public void saveEvent(Event event) {

		log.info("START - DefaultClientService:saveEvent()");

		String topicExchangePrice = env.getProperty("name.topicexchange.eventoapp");
		String routingKey = env.getProperty("name.routingKey.eventoapp");

		template.convertAndSend(topicExchangePrice, routingKey, event);
		
		log.info("END - DefaultClientService:saveEvent()");
	}

	@Override
	public void deleteEvent(long code) {

		log.info("START - DefaultClientService:deleteEvent()");
		
		client.deleteEvent(code);

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
			log.error("Error when request event's list from API!");
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
			log.error("Error when request event's list from API!");
		}

		log.info("END - DefaultClientService:seekRoleByName()");
		return role;
	}
}
