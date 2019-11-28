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
import de.swprojekt.speeddating.repository.IEventRepository;

/*
 * REST ist Architekturstil und bietet einheitliche Schnittstellen als Webservice an
 * Somit koennen ueber HTTP-Methoden wie GET/POST Entities abgerufen/geaendert/erstellt werden 
 */
@RestController
public class EventController {
	@Autowired
	private IEventRepository iEventRepository;

	@GetMapping("/events") // Anzeige existierender Events (http://localhost:8080/speeddating-web-7.0-SNAPSHOT/events/)
	public List<Event> getAll() {
		return iEventRepository.findAll();
	}

	@GetMapping("/events/{id}") // suchen von einzelnem Event
	public Event getOne(@PathVariable int id) {
		Optional<Event> gefundenesEvent;
		try {
			gefundenesEvent = iEventRepository.findById(id);

			return gefundenesEvent.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein Event zu ID vorhanden!");
			return null;
		}
	}

	@PostMapping("/events") // hinzufuegen von einem Event
	public Event createEvent(@RequestBody Event einEvent) {
		return iEventRepository.save(einEvent);
	}

	@PutMapping("/events/{id}") // aendern von existierendem Event
	public Event alterEvent(@PathVariable int id, @RequestBody Event einEvent) {
		Optional<Event> gefundenesEvent;
		try {
			gefundenesEvent = iEventRepository.findById(id);
			gefundenesEvent.get().setBezeichnung(einEvent.getBezeichnung());
			gefundenesEvent.get().setStartzeitpunkt(einEvent.getStartzeitpunkt());
			gefundenesEvent.get().setEndzeitpunkt(einEvent.getEndzeitpunkt());
			gefundenesEvent.get().setAbgeschlossen(einEvent.isAbgeschlossen());
			gefundenesEvent.get().setTeilnehmendeStudierende(einEvent.getTeilnehmendeStudierende());
			return iEventRepository.save(gefundenesEvent.get());
		} catch (NoSuchElementException e) {
			System.out.println("Kein Event zu ID vorhanden!");
			return null;
		}
	}

	@DeleteMapping("/events/{id}") // loeschen von existierendem Event
	public boolean deleteEvent(@PathVariable int id) {
		try {
			iEventRepository.deleteById(id);
			return true;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Kein Event zu ID vorhanden!");
			return false;
		}
	}

}
