package com.eventoApp.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoApp.models.Event;
import com.eventoApp.models.Guest;
import com.eventoApp.services.ClientService;

@RestController
@RequestMapping
public class EventAppController implements ErrorController {

	@Autowired
	private ClientService sr;
	
	@Autowired
	private WebClient webClient;
	
	private Logger log = LoggerFactory.getLogger(EventAppController.class);
	
	private static final String PATH = "/error";
	

	@GetMapping("/")
	public ModelAndView index() {
		log.info("EventoController:index()");
		return new ModelAndView("forward:/eventos");
	}
	
	
	@GetMapping("/showMyLoginPage")
	public ModelAndView loginPage() {
		ModelAndView loginPage = new ModelAndView("login");
		return loginPage;
	}
	
	
	
	// add request mapping for /access-denied
	@GetMapping("/access-denied")
	public ModelAndView showAccessDenied() {
		return new ModelAndView("proibido");
	}
	
	
	

	@GetMapping(value="/cadastrarEvento")
	public ModelAndView form() {
		return new ModelAndView("evento/formEvento");
	}

	
	//MÃ©todo chamado pelo form da pÃ¡gina formEvento.html
	@PostMapping(value="/cadastrarEvento")
	public String form(@Valid Event evento, BindingResult result, RedirectAttributes attributes) {
		
			//Se ocorrer um erro no input de dados, uma mensagem Ã© exibida para o usuÃ¡rio
			if (result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos!");
				return "redirect:cadastrarEvento";
			}

		sr.saveEvent(evento);
		
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
		return "redirect:cadastrarEvento";
	}
	
	
	@GetMapping("/eventos")
	public ModelAndView listEvents() {

		log.info("START - EventAppController:listEvents()");
		
		ModelAndView mv = new ModelAndView("index");
		
		//List<Event> list = sr.listEvents();
		
		Event teste = new Event();
		teste.setCity("S�o Paulo");
		teste.setCode(11);
		teste.setName("Luiz");
		
		List<Event> list = Arrays.asList(teste);
		
		mv.addObject("events", list);
		
		String usuario = getLoggedinUserName();
		
			if (!usuario.equals("")) {
				mv.addObject("usuario", usuario);
				log.info("EventoController:listaEventos() - USUARIO LOGADO: " + usuario);

			} else {
				mv.addObject("usuario", "visitante");
				log.info("EventoController:listaEventos() - NENHUM USUARIO LOGADO!");
			}
			
		log.info("END - EventAppController:listEvents()");
		return mv;
	}
	
	
	
	
	private String getLoggedinUserName() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		
		return principal.toString();
	}
	
	


	//Called when click a specific event
	@GetMapping(value="/eventDetail/{code}")
	public ModelAndView eventDetail(@PathVariable("code") long code) {
		
		log.info("START - EventAppController:eventDetail()");
		
		Event soughtEvent = sr.seekEvent(code);
		
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("event", soughtEvent);
		
		List<Guest> guests = sr.guestList(soughtEvent.getCode());
		
		mv.addObject("guests", guests);
		
		log.info("END - EventAppController:eventDetail()");
		return mv;
	}
	
	


	
	//Chamado ao cadastrar um convidado na pÃ¡gina detalhesEvento.html
	@PostMapping(value="/saveGuest/{eventCode}")
	public String saveGuest(@PathVariable("eventCode") long eventCode, @Valid Guest guest, BindingResult result, RedirectAttributes attributes) {
		
		log.info("EventoController:saveGuest()");
		
			if (result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos!");
				return "redirect:/{codigo}";
			}
		
		sr.saveGuest(eventCode, guest);
		
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";
	}
	

	@DeleteMapping("/deleteEvent/{code}")
	public ModelAndView deleteEvent(@PathVariable("code") long code) {
		
		sr.deleteEvent(code);
		return new ModelAndView("forward:/eventos");
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
