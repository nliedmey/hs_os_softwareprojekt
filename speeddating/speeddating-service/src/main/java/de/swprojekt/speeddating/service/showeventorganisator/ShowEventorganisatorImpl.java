package de.swprojekt.speeddating.service.showeventorganisator;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.repository.IEventorganisatorRepository;

/*
 * Implementierung des IShowEventorganisatorService-Interfaces
 * Eingebunden in Views wird hingegen nur das Interface! 
 * (hierfuer muss die Implementierung als Service deklariert sein!)
 */
@Service
public class ShowEventorganisatorImpl implements IShowEventorganisatorService {

	@Autowired // Repository wird autowired
	IEventorganisatorRepository iEventorganisatorRepository; // Zugriff auf Eventorganisator-Entities in DB

	@Override
	public List<Eventorganisator> showEventorganisatoren() {
		return iEventorganisatorRepository.findAll(); // gibt Liste 
	}

	@Override
	public Eventorganisator showEventorganisator(int eventorganisator_id) {
		Optional<Eventorganisator> gefundenerEventorganisator;
		try {
			gefundenerEventorganisator = iEventorganisatorRepository.findById(eventorganisator_id);
			return gefundenerEventorganisator.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein Eventorganisator zu ID vorhanden!");
			return null;
		}
	}

}
