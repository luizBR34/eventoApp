package com.eventoApp.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping
public class EventAppController implements ErrorController {


	private static final String PATH = "/error";

	@Value(value = "${EVENTOANGULAR_HOST}")
	private String eventoAngularHost;

	
	// add request mapping for /access-denied
	@GetMapping("/access-denied")
	public RedirectView showAccessDenied() {

		log.info("EventAppController:showAccessDenied()");
	    RedirectView redirect = new RedirectView();
	    redirect.setUrl(eventoAngularHost + "/acessDenied");
	    return redirect;
	}
	


	@GetMapping(value="/mainPage")
	public RedirectView mainPage() {
		
		log.info("EventAppController:mainPage()");
	    RedirectView redirect = new RedirectView();
	    redirect.setUrl(eventoAngularHost + "/eventos");
	    return redirect;
	}


	@GetMapping(value = PATH)
    public RedirectView handleError(HttpServletRequest request) {

    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    RedirectView redirect = new RedirectView();
	    
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
         
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
            	redirect.setUrl(eventoAngularHost + "/notFound");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            	redirect.setUrl(eventoAngularHost + "/error");
            	
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
            	redirect.setUrl(eventoAngularHost + "/forbidden");
            }
        }

        return redirect;
    }

	@Override
	public String getErrorPath() {
		return PATH;
	}
}
