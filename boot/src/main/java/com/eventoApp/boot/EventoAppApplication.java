package com.eventoApp.boot;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/* INSTRUCTIONS FOR BUILD USING DOCKERFILE
1) Cean Root Module
2) Build Models Module
3) Build Config Module
4) Build Root Module

    docker build -t luizpovoa/eventoapp:0.0.1-SNAPSHOT .
    docker run -p 8443:8443 -d -e VAULT_HOST=172.18.0.2 -e VAULT_ROOT_TOKEN=s.MvyZ4i7suJRAzO3tMVCRadPC -e EVENTOAPP_HOST=https://172.18.0.8:8443 -e EVENTOAS_HOST=https://172.18.0.6:8081 -e EVENTO_CACHE_URI=http://172.18.0.9:8585 -e EVENTO_WS_URI=http://172.18.0.11:9090 -e EVENTOANGULAR_HOST=http://172.18.0.5:4200 --network eventoapp-network --name EventoApp luizpovoa/eventoapp:0.0.1-SNAPSHOT
*/

@SpringBootApplication
@EnableFeignClients("com.eventoRS.clients")
@ComponentScan({"com.eventoApp", "com.eventoRS.services"})
public class EventoAppApplication extends SpringBootServletInitializer {

    static {
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier(){

                    public boolean verify(String hostname,
                                          javax.net.ssl.SSLSession sslSession) {
                        if (hostname.equals("localhost")) {
                            return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EventoAppApplication.class);
    }
 
    public static void main(String[] args) {
        SpringApplication.run(EventoAppApplication.class, args);
    }
    
    
	@Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }

    private Connector redirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }

}
