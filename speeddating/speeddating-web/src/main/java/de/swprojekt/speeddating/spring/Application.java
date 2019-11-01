package de.swprojekt.speeddating.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * The entry point of the Spring Boot application.
 */
@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
@ComponentScan({"de.swprojekt"})	//hier wird auf andere Pakete verwiesen, welches nach @Component durchsucht wird
@EnableJpaRepositories({"de.swprojekt"}) //wichtig fuer jpa
@EntityScan({"de.swprojekt"})	//alle Packages unter de.marius werden durchsucht nach Entity-Annotations
//@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
