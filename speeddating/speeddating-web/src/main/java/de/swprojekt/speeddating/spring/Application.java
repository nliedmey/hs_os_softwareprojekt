package de.swprojekt.speeddating.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Startpunkt der Webapplikation (Start via Rechtsklick auf diese Datei -> Run on Server ... -> Wildfly)
 */	
@EnableAutoConfiguration	//automatische Konfiguration des BeanContext (z.B. Einbindung wahrscheinlich verwendeter Abhaengigkeiten)
@EnableWebSecurity	//SpringSecurity wird in Webanwendung verwendet
@ComponentScan({"de.swprojekt"})	//hier wird auf andere Pakete verwiesen, welche inkl. Unterpaketen nach @Component durchsucht wird
@EnableJpaRepositories({"de.swprojekt"}) //Suche nach Repositories
@EntityScan({"de.swprojekt"})	//alle Packages unter de.swprojekt werden durchsucht nach Entity-Annotations
public class Application extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {	//Quelldaten der Anwendung hinzufuegen
        return application.sources(Application.class);
    }
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);	//Starten der Anwendung
    }
}

