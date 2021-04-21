package com.eventoApp.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eventoApp.configs.Oauth2AuthenticationSuccessHandler;
import com.eventoApp.models.Event;
import com.eventoApp.models.Guest;
import com.eventoApp.models.User;
import com.eventoApp.services.ClientService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class EventAppController implements ErrorController {

	@Autowired
	private ClientService sr;
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	@Qualifier("oauth2authSuccessHandler")
	private Oauth2AuthenticationSuccessHandler authenticationHandler;
	
	private Logger log = LoggerFactory.getLogger(EventAppController.class);
	
	private static final String PATH = "/error";
	
	
	
	
	@GetMapping("/loggedUser")
	public Mono<User> recuperaUsuarioLogado() {
		
		Mono<String> loggedUserMono = authenticationHandler.getLoggedUserName(SecurityContextHolder.getContext().getAuthentication());
		
		String loggedUser = loggedUserMono.block();
		
		if (!loggedUser.equals("")) {
			log.info("EventAppController:recuperaUsuarioLogado() - USUARIO LOGADO: " + loggedUser);

		} else {
			log.info("EventAppController:recuperaUsuarioLogado() - NENHUM USUARIO LOGADO!");
		}

		return Mono.just(User.builder().userName(loggedUser).build());
	}
	
	
	// add request mapping for /access-denied
	@GetMapping("/access-denied")
	public ModelAndView showAccessDenied() {
		return new ModelAndView("proibido");
	}
	


/*	@GetMapping(value="/saveEvent")
	public ModelAndView form() {
		return new ModelAndView("evento/formEvento");
	}*/
	
	
	@GetMapping(value="/saveEvent")
	public RedirectView saveEventForm() {
		
		log.info("EventAppController:formCadastrarEvento()");
	    RedirectView redirect = new RedirectView();
	    redirect.setUrl("http://localhost:4200/cadastrarEvento");
	    return redirect;
	}
	
	
	
	@PostMapping(value="/saveEvent")
	public void saveEvent(@RequestBody @Valid Event event) {
		
		sr.saveEvent(event);
	}
	

	
	@GetMapping(value="/events", produces="application/json")
	public @ResponseBody List<Event> getEventList() {
		
		log.info("EventAppController:getEventList()");
		
/*		Event um = new Event(1, "Casamento do Fulano", "S�o PAulo", "12/04/2021", "11:00");
		Event dois = new Event(2, "Cinema", "Rio de Janeiro", "16/08/2018", "18:00");
		List<Event> eventList = Arrays.asList(um, dois);*/

        List<Event> eventList = sr.eventList();

		return eventList;
	}
	


	@GetMapping(value="/seekEvent/{code}", produces="application/json")
	public @ResponseBody Event seekEvent(@PathVariable("code") long code) {
		
		log.info("START - EventAppController:seekEvent()");
		
		Event soughtEvent = sr.seekEvent(code);
		
		log.info(soughtEvent + "/n" + "END - EventAppController:seekEvent()");
		
		return soughtEvent;
	}
	
	
	
	
	@GetMapping(value="/guestList/{eventCode}", produces="application/json")
	public @ResponseBody List<Guest> guestList(@PathVariable("eventCode") long eventCode) {
		
		log.info("EventAppController:guestList()");
		
		List<Guest> listaConvidados = sr.guestList(eventCode);

		return listaConvidados;
	}


	@PostMapping(value="/saveGuest/{eventCode}", produces="application/json")
	public @ResponseBody void saveGuest(@PathVariable("eventCode") long eventCode, @Valid Guest guest) {
		
		log.info("EventAppController:saveGuest()");
		sr.saveGuest(eventCode, guest);
	}
	
	
	
	@DeleteMapping("/deleteEvent/{code}")
	public @ResponseBody void deleteEvent(@PathVariable("code") long code) {
		
		log.info("EventAppController:deleteEvent()");
		sr.deleteEvent(code);
	}
	

	
	@DeleteMapping("/deleteGuest/{id}")
	public String deleteGuest(@PathVariable("id")long id) {
		
		Event event = sr.deleteGuest(id);
		
		long eventCode = event.getCode();
		String code = "" + eventCode;
		
		return "redirect:/" + code; // chama o mÃ©todo detalhesEvento(@PathVariable("codigo") long codigo) mostrando a lista de eventos
	}


	@GetMapping("/hello")
	public String sayHello(Model model, @RequestParam(defaultValue = "Siva" ,required = false)String name) throws Exception, URISyntaxException {
		
		model.addAttribute("name", name);
		
		String response = webClient.get().uri(new URI("http://localhost:8084/hello")).retrieve().bodyToMono(String.class).block();
		System.out.println("RESPOSTA DO RESOURCE SERVER: {}" + response);
		
		return "hello";
	}
	
	
	

	@GetMapping(value = PATH)
    public ModelAndView manipulaError(HttpServletRequest request) {

    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
         
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
            	return new ModelAndView("perdido");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            	return new ModelAndView("error");
                
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
            	return new ModelAndView("proibido");
            }
        }

        return new ModelAndView("error");
    }


    @Override
    public String getErrorPath() {
        return PATH;
    }

}
