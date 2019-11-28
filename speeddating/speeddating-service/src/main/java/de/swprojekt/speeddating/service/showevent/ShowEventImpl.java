package de.swprojekt.speeddating.service.showevent;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.repository.IEventRepository;

/*
 * Implementierung des IShowEventService-Interfaces
 * Eingebunden in Views wird hingegen nur das Interface! 
 * (hierfuer muss die Implementierung als Service deklariert sein!)
 */
@Service
public class ShowEventImpl implements IShowEventService {

	@Autowired // Repository wird autowired
	IEventRepository iEventRepository; // Zugriff auf Event-Entities in DB

	@Override
	public List<Event> showEvents() {
		return iEventRepository.findAll(); // gibt Liste aller Studierender zurueck
	}

	@Override
	public Event showEvent(int event_id) {
		Optional<Event> gefundenesEvent;
		try {
			gefundenesEvent = iEventRepository.findById(event_id);
			return gefundenesEvent.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein Studierender zu ID vorhanden!");
			return null;
		}
	}
}
