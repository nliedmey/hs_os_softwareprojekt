package de.swprojekt.speeddating.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringBootWebApplication {

	@RequestMapping("/")	//Standardverzeichnis ueber localhost:8080 erreichbar nach Starten
	String home()
	{
		return "Hallo Welt";
	}
	
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
}
