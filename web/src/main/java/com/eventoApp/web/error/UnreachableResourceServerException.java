package com.eventoApp.web.error;

import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;

public class UnreachableResourceServerException extends ClientAuthorizationRequiredException {
    /**
     * Constructs a {@code ClientAuthorizationRequiredException} using the provided parameters.
     *
     * @param clientRegistrationId the identifier for the client's registration
     */
    public UnreachableResourceServerException(String clientRegistrationId) {
        super(clientRegistrationId);
    }
}
