package com.eventoApp.web;

import com.eventoApp.models.User;
import com.eventoRS.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class EventAppController implements ErrorController {

	@Autowired
	private ClientService sr;

	@Autowired
	@Qualifier("sessionsPersisted")
	private SessionRegistry session;

	private static final String PATH = "/error";

	
	// add request mapping for /access-denied
	@GetMapping("/access-denied")
	public RedirectView showAccessDenied() {

		log.info("EventAppController:showAccessDenied()");
	    RedirectView redirect = new RedirectView();
	    redirect.setUrl("http://localhost:4200/acessDenied");
	    return redirect;
	}
	


	@GetMapping(value="/mainPage")
	public RedirectView mainPage() {
		
		log.info("EventAppController:mainPage()");
	    RedirectView redirect = new RedirectView();
	    redirect.setUrl("http://localhost:4200/eventos");
	    return redirect;
	}


	@GetMapping(value="/oauth2/authorization/eventoas")
	public void teste() {

		log.info("EventAppController:mainPage()");
	}



	public User getAuthenticatedUser() {

		List<Object> principals = session.getAllPrincipals();

		User loggedUser = (!principals.isEmpty()) ?
				principals.get(0) instanceof org.springframework.security.core.userdetails.User ?
						User.builder().userName(((org.springframework.security.core.userdetails.User) principals.get(0)).getUsername()).build() :
						User.builder().userName(((org.springframework.security.oauth2.core.user.DefaultOAuth2User) principals.get(0)).getAttributes().containsKey("username") ?
								((org.springframework.security.oauth2.core.user.DefaultOAuth2User) principals.get(0)).getAttributes().get("username").toString() :
								((org.springframework.security.oauth2.core.user.DefaultOAuth2User) principals.get(0)).getAttributes().get("name").toString()).build() :
				sr.getSession();
		return loggedUser;
	}
	
	

	@GetMapping(value = PATH)
    public RedirectView handleError(HttpServletRequest request) {

    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    RedirectView redirect = new RedirectView();
	    
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
         
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
            	redirect.setUrl("http://localhost:4200/notFound");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            	redirect.setUrl("http://localhost:4200/error");
            	
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
            	redirect.setUrl("http://localhost:4200/forbidden");
            }
        }

        return redirect;
    }


    @Override
    public String getErrorPath() {
        return PATH;
    }

}
