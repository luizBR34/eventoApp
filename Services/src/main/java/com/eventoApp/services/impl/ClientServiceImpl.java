package com.eventoApp.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.env.Environment;

import com.eventoApp.models.Event;
import com.eventoApp.models.Guest;
import com.eventoApp.models.Role;
import com.eventoApp.models.User;
import com.eventoApp.services.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

	private Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

	@Autowired
	@Qualifier("template")
	private AmqpTemplate template;

	@Autowired
	private Environment env;

	@Value(value = "${eventocache.endpoint.uri}")
	private String eventoCacheEndpointURI;

	RestTemplate restTemplate = new RestTemplate();

	@Override
	public List<Event> eventList() {

		log.info("START - ClientServiceImpl:listEvents()");

		ResponseEntity<List<Event>> responseEntity = restTemplate.exchange(eventoCacheEndpointURI, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Event>>() {
				});
		List<Event> listOfEvents = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("ClientServiceImpl:listEvents() - EventoCache API responded the request successfully!");
			listOfEvents = responseEntity.getBody();
		} else {
			log.error("Error when request event's list from API!");
		}

		log.info("END - ClientServiceImpl:listEvents()");
		return listOfEvents;
	}

	@Override
	public Event seekEvent(long code) {

		log.info("START - ClientServiceImpl:searchEvent()");

		String path = eventoCacheEndpointURI + "/searchEvent/" + String.valueOf(code);

		ResponseEntity<Event> responseEntity = restTemplate.exchange(path, HttpMethod.GET, null, Event.class);
		Event event = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("ClientServiceImpl:searchEvent() - EventoCache API responded the request successfully!");
			event = responseEntity.getBody();
		} else {
			log.error("Error when request event's list from API!");
		}

		log.info("END - ClientServiceImpl:searchEvent()");
		return event;
	}

	@Override
	public User seekUser(String login) {

		log.info("START - ClientServiceImpl:seekUser()");

		String path = eventoCacheEndpointURI + "/seekUser/" + login;

		ResponseEntity<User> responseEntity = restTemplate.exchange(path, HttpMethod.GET, null, User.class);
		User user = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("ClientServiceImpl:seekUser() - EventoCache API responded the request successfully!");
			user = responseEntity.getBody();
		} else {
			log.error("Error when request event's list from API!");
		}

		log.info("END - ClientServiceImpl:seekUser()");
		return user;
	}
	
	
	@Override
	public void saveUser(User user) {
		
		log.info("START - ClientServiceImpl:saveUser()");

		String topicExchangePrice = env.getProperty("name.topicexchange.assortment");
		String routingKey = env.getProperty("name.routingKey.updates");

		template.convertAndSend(topicExchangePrice, routingKey, user);

		log.info("END - ClientServiceImpl:saveUser()");
	}
	

	@Override
	public List<Guest> guestList(long eventCode) {

		log.info("START - ClientServiceImpl:listGuests()");

		String path = eventoCacheEndpointURI + "/listGuests/" + String.valueOf(eventCode);

		ResponseEntity<List<Guest>> responseEntity = restTemplate.exchange(path, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Guest>>() {
				});
		List<Guest> guestList = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("ClientServiceImpl:listGuests() - EventoCache API responded the request successfully!");
			guestList = responseEntity.getBody();
		} else {
			log.error("Error when request event's list from API!");
		}

		log.info("END - ClientServiceImpl:listGuests()");
		return guestList;
	}

	@Override
	public void saveGuest(long eventCode, Guest guest) {

		log.info("START - ClientServiceImpl:saveGuest()");

		String topicExchangePrice = env.getProperty("name.topicexchange.assortment");
		String routingKey = env.getProperty("name.routingKey.updates");

		template.convertAndSend(topicExchangePrice, routingKey, guest);

		log.info("END - ClientServiceImpl:saveGuest()");
	}

	@Override
	public void saveEvent(Event event) {

		log.info("START - ClientServiceImpl:saveEvent()");

		String topicExchangePrice = env.getProperty("name.topicexchange.assortment");
		String routingKey = env.getProperty("name.routingKey.updates");

		template.convertAndSend(topicExchangePrice, routingKey, event);
		
		log.info("END - ClientServiceImpl:saveEvent()");
	}

	@Override
	public void deleteEvent(long code) {

		log.info("START - ClientServiceImpl:deleteEvent()");

		String path = eventoCacheEndpointURI + "/deletaEvento/" + String.valueOf(code);

		Map<String, Long> params = new HashMap<String, Long>();
		params.put("code", code);

		restTemplate.delete(path, params);

		log.info("END - ClientServiceImpl:deleteEvent()");
	}

	public Event deleteGuest(long id) {

		log.info("START - ClientServiceImpl:deleteGuest()");

		String path = eventoCacheEndpointURI + "/deletaConvidado/" + String.valueOf(id);

		ResponseEntity<Event> responseEntity = restTemplate.exchange(path, HttpMethod.DELETE, null, Event.class);
		Event event = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("ClientServiceImpl:deleteGuest) - EventoWS API responded the request successfully!");
			event = responseEntity.getBody();
		} else {
			log.error("Error when request event's list from API!");
		}

		log.info("END - ClientServiceImpl:deleteGuest()");
		return event;
	}

	@Override
	public Role seekRoleByName(String theRoleName) {

		log.info("START - ClientServiceImpl:seekRoleByName()");

		String path = eventoCacheEndpointURI + "/seekRole/" + theRoleName;

		ResponseEntity<Role> responseEntity = restTemplate.exchange(path, HttpMethod.GET, null, Role.class);
		Role role = null;

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("ClientServiceImpl:seekRoleByName() - EventoCache API responded the request successfully!");
			role = responseEntity.getBody();
		} else {
			log.error("Error when request event's list from API!");
		}

		log.info("END - ClientServiceImpl:seekRoleByName()");
		return role;
	}
}
