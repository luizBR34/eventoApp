package com.eventoApp.services.impl;

import java.util.List;

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
	
	
	public List<Event> listEvents() {
		
		log.info("START - ClientServiceImpl:listEvents()");
		
		ResponseEntity<List<Event>> responseEntity = restTemplate.exchange(eventoCacheEndpointURI, HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() { });
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
	public Event searchEvent(long code) {

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

	
	
	/*
	public Usuario buscaUsuario(String login) {
		
		log.info("ClientServiceImpl:buscaUsuario()");
		
		ClientResponse resposta = resource.path("buscaUsuario/" + login)
										  .accept(MediaType.APPLICATION_JSON)
										  .get(ClientResponse.class);

			if (resposta.getStatusInfo() == ClientResponse.Status.OK) {
				
				log.info("ClientServiceImpl:buscaUsuario().ClientResponse.getStatusInfo() = OK");
				Usuario usuario = resposta.getEntity(Usuario.class);
				return usuario;
	    		
			} else {
				
				log.warn("ClientServiceImpl:buscaUsuario().ClientResponse.getStatusInfo() = " + resposta.getStatusInfo().getStatusCode());
				return null;
			}	
	}
	*/
	
	
	public List<Guest> listGuests(long eventCode) {		
		
		log.info("START - ClientServiceImpl:listGuests()");
		
		String path = eventoCacheEndpointURI + "/listGuests/" + String.valueOf(eventCode);
		
		ResponseEntity<List<Guest>> responseEntity = restTemplate.exchange(path, HttpMethod.GET, null, new ParameterizedTypeReference<List<Guest>>() { });
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
	
	/*
	public boolean cadastraConvidado(long codigoEvento, Convidado convidado) {
		
		log.info("ClientServiceImpl:cadastraConvidado()");
		
		ClientResponse resposta = resource.path("cadastraConvidado/" + String.valueOf(codigoEvento))
										  .type(MediaType.APPLICATION_JSON)
				  						  .accept(MediaType.APPLICATION_JSON)
				  						  .post(ClientResponse.class, convidado);

			if (resposta.getStatusInfo() == ClientResponse.Status.OK) {
			
				log.info("ClientServiceImpl:cadastraConvidado().ClientResponse.getStatusInfo() = OK");
				return true;
			
			} else {
			
				log.warn("ClientServiceImpl:cadastraConvidado().ClientResponse.getStatusInfo() = " + resposta.getStatusInfo().getStatusCode());
				return false;
			}
	}
	*/
	
	
	public void saveEvent(Event event) {

		log.info("ClientServiceImpl:saveEvent()");
		
        String topicExchangePrice = env.getProperty("name.topicexchange.assortment");
        String routingKey = env.getProperty("name.routingKey.updates");
        
        template.convertAndSend(topicExchangePrice, routingKey, event);
	}

	
	/*
	public void deletaEvento(long codigo) {
		
		log.info("ClientServiceImpl:deletaEvento()");
		
		ClientResponse resposta = resource.path("deletaEvento/" + String.valueOf(codigo))
								.accept(MediaType.APPLICATION_JSON)
								.delete(ClientResponse.class);
		
			if (resposta.getStatusInfo() == ClientResponse.Status.OK) {
				log.info("ClientServiceImpl:deletaEvento().ClientResponse.getStatusInfo() = OK");
			} else {
				log.warn("ClientServiceImpl:deletaEvento().ClientResponse.getStatusInfo() = " + resposta.getStatusInfo().getStatusCode());
			}
	}
	
	
	
	public Evento deletaConvidado(String rg) {
		
		log.info("ClientServiceImpl:deletaConvidado()");
		
		ClientResponse resposta = resource.path("deletaConvidado/" + rg)
										  .accept(MediaType.APPLICATION_JSON)
										  .delete(ClientResponse.class);
		
			if (resposta.getStatusInfo() == ClientResponse.Status.OK) {
				log.info("ClientServiceImpl:deletaConvidado().ClientResponse.getStatusInfo() = OK");
				Evento evento = resposta.getEntity(Evento.class);
				return evento;
			} else {
				log.warn("ClientServiceImpl:deletaConvidado().ClientResponse.getStatusInfo() = " + resposta.getStatusInfo().getStatusCode());
				return null;
			}
	}
	*/
}
