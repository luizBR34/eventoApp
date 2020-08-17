package com.eventoApp.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoApp.model.Guest;
import com.eventoApp.model.Event;
import com.eventoApp.service.ClientService;

@Controller
public class EventAppController implements ErrorController {

	@Autowired
	private ClientService sr;
	
    @Autowired
    private SessionRegistry registroSecao;
	
	private Logger log = LoggerFactory.getLogger(EventAppController.class);
	
	private static final String PATH = "/error";
	

	@RequestMapping("/")
	public String index() {
		log.info("EventoController:index()");
		return "redirect:/eventos";
	}
	
	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}

	
	//Método chamado pelo form da página formEvento.html
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.POST)
	public String form(@Valid Event evento, BindingResult result, RedirectAttributes attributes) {
		
			//Se ocorrer um erro no input de dados, uma mensagem é exibida para o usuário
			if (result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos!");
				return "redirect:cadastrarEvento";
			}

		sr.saveEvent(evento);
		
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
		return "redirect:cadastrarEvento";
	}
	
	
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {

		log.info("EventoController:listaEventos()");
		
		ModelAndView mv = new ModelAndView("index");
		
		Iterable<Event> lista = sr.listEvents();
		
		logRequisicao("listaEventos()", lista);
		
		mv.addObject("eventos", lista); //adiciona à página
		
		List<String> usuarios = recuperaUsuariosLogados();
		
			if (!usuarios.isEmpty()) {
				mv.addObject("usuario", usuarios.get(0));
				log.info("EventoController:listaEventos() - USUARIO LOGADO: " + usuarios.get(0));

			} else {
				mv.addObject("usuario", "visitante");
				log.info("EventoController:listaEventos() - NÃO HÁ NENHUM USUARIO LOGADO!");
			}
			
		usuarios.clear();
		return mv;
	}
	
	
	
	
	public List<String> recuperaUsuariosLogados() {
	    return registroSecao.getAllPrincipals().stream()
	    									   .filter(u -> !registroSecao.getAllSessions(u, false).isEmpty())
	    									   .map(u -> User.class.cast(u).getUsername())
	    									   .collect(Collectors.toList());
	}
	
	

	/*
	//Chamado ao clicar num item de evento
	@RequestMapping(value="/{codigo}", method=RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		
		log.info("EventoController:detalhesEvento()");
		
		Evento evento = sr.buscaEvento(codigo);
		
		logRequisicao("detalhesEvento()", evento);
		
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("event", evento);
		
		Iterable<Convidado> convidados = sr.listaConvidados(evento);
		
		logRequisicao("detalhesEvento()", convidados);
		
		mv.addObject("convidados", convidados);
		return mv;
	}
	*/
	


	
	//Chamado ao cadastrar um convidado na página detalhesEvento.html
	@RequestMapping(value="/{codigo}", method=RequestMethod.POST)
	public String cadastrarConvidado(@PathVariable("codigo") long codigo, @Valid Guest convidado, BindingResult result, RedirectAttributes attributes) {
		
		log.info("EventoController:cadastrarConvidado()");

			//Se ocorrer um erro no input de dados, uma mensagem é exibida para o usuário
			if (result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos!");
				return "redirect:/{codigo}";
			}
		
		//sr.cadastraConvidado(codigo, convidado);
		
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";
	}
	

	@RequestMapping("/deletarEvento")
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
		
		return "redirect:/" + codigo; // chama o método detalhesEvento(@PathVariable("codigo") long codigo) mostrando a lista de eventos
	}
	*/
	

	@RequestMapping("/login")
	public String carregaPaginaLogin() {
		return "login";
	}
	

    @RequestMapping(value = PATH)
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
    
    
	public void logRequisicao(String nomeMetodo, Object o) {
		if (o != null) {
			log.info("EventoController:" + nomeMetodo + "." + o.getClass().getName() + " criado com sucesso!");
		} else {
			log.error("ERROR: " + "EventoController:" + nomeMetodo);
		}
	}
}
