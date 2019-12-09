package de.swprojekt.speeddating.service.rest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.repository.IEventRepository;
import de.swprojekt.speeddating.repository.IEventorganisatorRepository;

/*
 * REST ist Architekturstil und bietet einheitliche Schnittstellen als Webservice an
 * Somit koennen ueber HTTP-Methoden wie GET/POST Entities abgerufen/geaendert/erstellt werden 
 */
@RestController
public class EventorganisatorController {
	@Autowired
	private IEventorganisatorRepository iEventorganisatorRepository;

	@GetMapping("/eventorganisator") // Anzeige existierender Eventorganisatoren (http://localhost:8080/speeddating-web-7.0-SNAPSHOT/eventorganisator/)
	public List<Eventorganisator> getAll() {
		return iEventorganisatorRepository.findAll();
	}

	@GetMapping("/eventorganisator/{id}") // suchen von einzelnem Eventorganisator
	public Eventorganisator getOne(@PathVariable int id) {
		Optional<Eventorganisator> gefundenerEventorganisator;
		try {
			gefundenerEventorganisator = iEventorganisatorRepository.findById(id);

			return gefundenerEventorganisator.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein Event zu ID vorhanden!");
			return null;
		}
	}

	@PostMapping("/eventorganisator") // hinzufuegen von einem Eventorganisator
	public Eventorganisator createEventorganisator(@RequestBody Eventorganisator einEventorganisator) {
		return iEventorganisatorRepository.save(einEventorganisator);
	}

	@PutMapping("/eventorganisator/{id}") // aendern von existierendem Eventorganisator
	public Eventorganisator alterEventorganisator(@PathVariable int id, @RequestBody Eventorganisator einEventorganisator) {
		Optional<Eventorganisator> gefundenerEventorganisator;
		try {
			gefundenerEventorganisator=iEventorganisatorRepository.findById(id);
			gefundenerEventorganisator.get().setVorname(einEventorganisator.getVorname());
			gefundenerEventorganisator.get().setNachname(einEventorganisator.getNachname());
			gefundenerEventorganisator.get().setFachbereich(einEventorganisator.getFachbereich());
			gefundenerEventorganisator.get().setTelefonnr(einEventorganisator.getTelefonnr());
			gefundenerEventorganisator.get().setEmail(einEventorganisator.getEmail());
			gefundenerEventorganisator.get().setVerwaltet_events(einEventorganisator.getVerwaltet_events());
			return iEventorganisatorRepository.save(gefundenerEventorganisator.get());
			
		} catch (NoSuchElementException e) {
			System.out.println("Kein Eventorganisator zu ID vorhanden!");
			return null;
		}
	}

	@DeleteMapping("/eventorganisator/{id}") // loeschen von existierendem Eventorganisator
	public boolean deleteEventorganisator(@PathVariable int id) {
		try {
			iEventorganisatorRepository.deleteById(id);
			return true;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Kein Eventorganisator zu ID vorhanden!");
			return false;
		}
	}

}
