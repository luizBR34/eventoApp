package com.eventoApp.service.impl;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.eventoApp.model.Evento;
import com.eventoApp.model.Convidado;
import com.eventoApp.model.Usuario;
import com.eventoApp.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
	
	private Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);
	
	private static final String REST_URI = "http://localhost:9090/eventoWS";
	
	private WebResource resource;
	
	
	public ClientServiceImpl() {
		
		DefaultClientConfig configPadrao = new DefaultClientConfig();
		configPadrao.getClasses().add(JacksonJsonProvider.class);
		
		Client client = Client.create(configPadrao);
		
		resource = client.resource(REST_URI);
		
	}
	

	
	public Iterable<Evento> listaEventos() {
		
		log.info("ClientServiceImpl:listaEventos()");
		
		ClientResponse resposta = resource.accept(MediaType.APPLICATION_JSON)
										  .get(ClientResponse.class);
		
		
			if (resposta.getStatusInfo() == ClientResponse.Status.OK) {
				
				log.info("ClientServiceImpl:listaEventos().ClientResponse.getStatusInfo() = OK");
				Iterable<Evento> listaEventos = resposta.getEntity(new GenericType<List<Evento>>() { });
				return listaEventos;
	    		
			} else {
				
				log.warn("ClientServiceImpl:listaEventos().ClientResponse.getStatusInfo() = " + resposta.getStatusInfo().getStatusCode());
				return null;
			}
	}

	
	public Evento buscaEvento(long codigo) {
		
		log.info("ClientServiceImpl:buscaEvento()");
		
		ClientResponse resposta = resource.path("buscaEvento/" + String.valueOf(codigo))
										  .accept(MediaType.APPLICATION_JSON)
										  .get(ClientResponse.class);

			if (resposta.getStatusInfo() == ClientResponse.Status.OK) {
				
				log.info("ClientServiceImpl:buscaEvento().ClientResponse.getStatusInfo() = OK");
				Evento evento = resposta.getEntity(Evento.class);
				return evento;
	    		
			} else {
				
				log.warn("ClientServiceImpl:buscaEvento().ClientResponse.getStatusInfo() = " + resposta.getStatusInfo().getStatusCode());
				return null;
			}
	}
	

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
	
	
	
	public Iterable<Convidado> listaConvidados(Evento evento) {		
		
		log.info("ClientServiceImpl:listaConvidados()");
		
		ClientResponse resposta = resource.type(MediaType.APPLICATION_JSON)
				  						  .accept(MediaType.APPLICATION_JSON)
				  						  .post(ClientResponse.class, evento);

			if (resposta.getStatusInfo() == ClientResponse.Status.OK) {
			
				log.info("ClientServiceImpl:listaConvidados().ClientResponse.getStatusInfo() = OK");
				Iterable<Convidado> convidados = resposta.getEntity(new GenericType<List<Convidado>>() { });
				return convidados;
			
			} else {
			
				log.warn("ClientServiceImpl:listaConvidados().ClientResponse.getStatusInfo() = " + resposta.getStatusInfo().getStatusCode());
				return null;
			}
	}
	
	
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
	
	
	
	public boolean cadastraEvento(Evento evento) {

		log.info("ClientServiceImpl:cadastraEvento()");
		
		ClientResponse resposta = resource.path("cadastraEvento")
										  .type(MediaType.APPLICATION_JSON)
				  						  .accept(MediaType.APPLICATION_JSON)
				  						  .post(ClientResponse.class, evento);

			if (resposta.getStatusInfo() == ClientResponse.Status.OK) {
			
				log.info("ClientServiceImpl:cadastraEvento().ClientResponse.getStatusInfo() = OK");
				return true;
			
			} else {
			
				log.warn("ClientServiceImpl:cadastraEvento().ClientResponse.getStatusInfo() = " + resposta.getStatusInfo().getStatusCode());
				return false;
			}
	}
	
	
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
}
