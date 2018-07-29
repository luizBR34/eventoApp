package com.eventoApp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoApp.model.Convidado;
import com.eventoApp.model.Evento;
import com.eventoApp.repository.ConvidadoRepository;
import com.eventoApp.repository.EventoRepository;

@Controller
public class EventoController implements ErrorController {
	
	@Autowired
	private EventoRepository rep;
	
	@Autowired
	private ConvidadoRepository cr;
	
	private static final String PATH = "/error";
	
	
	@RequestMapping("/")
	public String index() {
		return "redirect:/eventos"; //Retorna a Lista de Eventos
	}
	
	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}

	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.POST)
	public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		
			//Se ocorrer um erro no input de dados, uma mensagem é exibida para o usuário
			if (result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos!");
				return "redirect:cadastrarEvento";
			}
		
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
		rep.save(evento);
		return "redirect:cadastrarEvento";
	}
	
	
	
	
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index"); //Obtem um referencia da Página
		Iterable<Evento> lista = rep.findAll(); //Realiza uma busca no banco
		mv.addObject("eventos", lista); //adiciona à página
		return mv;
	}
	
	
	@RequestMapping(value="/{codigo}", method=RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		Evento evento = rep.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("eventos", evento);
		
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);
		
		return mv;
	}
	
	
	@RequestMapping(value="/{codigo}", method=RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {

			//Se ocorrer um erro no input de dados, uma mensagem é exibida para o usuário
			if (result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos!");
				return "redirect:/{codigo}";
			}
		
		Evento evento = rep.findByCodigo(codigo);
		convidado.setEvento(evento);
		cr.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";
	}
	

	@RequestMapping("/deletarEvento")
	public String deletarEvento(long codigo) {
		Evento evento = rep.findByCodigo(codigo);
		rep.delete(evento);
		return "redirect:/eventos"; //Retorna a Lista de Eventos
	}
	

	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String rg) {
		Convidado convidado = cr.findByRg(rg);
		cr.delete(convidado);
		
		Evento evento = convidado.getEvento();
		long codigoEvento = evento.getCodigo();
		String codigo = "" + codigoEvento;
		
		return "redirect:/" + codigo; // chama o método detalhesEvento(@PathVariable("codigo") long codigo) mostrando a lista de eventos
	}
	

    @RequestMapping(value = PATH)
    public String error() {
        return "Ocorreu um erro no carregamento das paginas!";
    }
    

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
