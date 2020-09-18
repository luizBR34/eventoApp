package com.eventoApp.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoApp.model.Event;
import com.eventoApp.model.Guest;
import com.eventoApp.service.ClientService;

@RestController
@RequestMapping
public class EventAppController implements ErrorController {

	@Autowired
	private ClientService sr;
	
    @Autowired
    private SessionRegistry registroSecao;
	
	private Logger log = LoggerFactory.getLogger(EventAppController.class);
	
	private static final String PATH = "/error";
	

	@GetMapping("/")
	public String index() {
		log.info("EventoController:index()");
		return "redirect:/eventos";
	}
	
	
	@GetMapping(value="/cadastrarEvento")
	public String form() {
		return "evento/formEvento";
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
		
		List<Event> list = sr.listEvents();
		
		verifyRequestedObject("listEvents()", list);
		
		mv.addObject("events", list);
		
		List<String> usuarios = recuperaUsuariosLogados();
		
			if (!usuarios.isEmpty()) {
				mv.addObject("usuario", usuarios.get(0));
				log.info("EventoController:listaEventos() - USUARIO LOGADO: " + usuarios.get(0));

			} else {
				mv.addObject("usuario", "visitante");
				log.info("EventoController:listaEventos() - NÃƒO HÃ� NENHUM USUARIO LOGADO!");
			}
			
		usuarios.clear();
		
		log.info("END - EventAppController:listEvents()");
		return mv;
	}
	
	
	
	
	public List<String> recuperaUsuariosLogados() {
	    return registroSecao.getAllPrincipals().stream()
	    									   .filter(u -> !registroSecao.getAllSessions(u, false).isEmpty())
	    									   .map(u -> User.class.cast(u).getUsername())
	    									   .collect(Collectors.toList());
	}
	
	


	//Called when click a specific event
	@GetMapping(value="/{codigo}")
	public ModelAndView eventDetail(@PathVariable("code") long code) {
		
		log.info("START - EventAppController:eventDetail()");
		
		Event soughtEvent = sr.searchEvent(code);
		
		verifyRequestedObject("eventDetail()", soughtEvent);
		
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("event", soughtEvent);
		
		List<Guest> guests = sr.listGuests(soughtEvent);
		
		verifyRequestedObject("eventDetail()", guests);
		
		mv.addObject("convidados", guests);
		
		log.info("END - EventAppController:eventDetail()");
		return mv;
	}
	
	


	
	//Chamado ao cadastrar um convidado na pÃ¡gina detalhesEvento.html
	@PostMapping(value="/{codigo}")
	public String cadastrarConvidado(@PathVariable("codigo") long codigo, @Valid Guest convidado, BindingResult result, RedirectAttributes attributes) {
		
		log.info("EventoController:cadastrarConvidado()");

			//Se ocorrer um erro no input de dados, uma mensagem Ã© exibida para o usuÃ¡rio
			if (result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos!");
				return "redirect:/{codigo}";
			}
		
		//sr.cadastraConvidado(codigo, convidado);
		
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";
	}
	

	@DeleteMapping("/deletarEvento")
	public String deletarEvento(long codigo) {
		
		//sr.deletaEvento(codigo);
		
		return "redirect:/eventos"; //Retorna a Lista de Eventos
	}
	

	/*
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String rg) {
		
		Evento evento = sr.deletaConvidado(rg);
		
		long codigoEvento = evento.getCodigo();
		String codigo = "" + codigoEvento;
		
		return "redirect:/" + codigo; // chama o mÃ©todo detalhesEvento(@PathVariable("codigo") long codigo) mostrando a lista de eventos
	}
	*/
	

	@GetMapping("/login")
	public String carregaPaginaLogin() {
		return "login";
	}
	

	@GetMapping(value = PATH)
    public String manipulaError(HttpServletRequest request) {

    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
         
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "perdido";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error";
                
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
            	return "proibido";
            }
        }

        return "error";
    }
    

    @Override
    public String getErrorPath() {
        return PATH;
    }
    
    
	public void verifyRequestedObject(String methodName, Object o) {
		if (o != null) {
			log.info("EventAppController:" + methodName + "." + o.getClass().getName() + " was succefully created!");
		} else {
			log.error("ERROR: " + "EventAppController:" + methodName);
		}
	}
    
}
