package de.swprojekt.speeddating.service.alterevent;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.repository.IEventRepository;

/*
 * Implementierung des IAlterEventService-Interfaces
 * Eingebunden in Views wird hingegen nur das Interface! 
 * (hierfuer muss die Implementierung als Service deklariert sein!)
 */
@Service
public class AlterEventImpl implements IAlterEventService {

	@Autowired // Repository wird autowired
	IEventRepository iEventRepository; // Zugriff auf Studierende-Entities in DB

	@Override
	public Event aenderEvent(Event veraendertesEventDAO) {
		try {
			Event gefundenesEvent = iEventRepository
					.findById(veraendertesEventDAO.getEvent_id()).get(); // existierendes Eventobjekt aus DB holen
			gefundenesEvent.setBezeichnung(veraendertesEventDAO.getBezeichnung());
			gefundenesEvent.setStartzeitpunkt(veraendertesEventDAO.getStartzeitpunkt());
			gefundenesEvent.setEndzeitpunkt(veraendertesEventDAO.getEndzeitpunkt());
			gefundenesEvent.setAbgeschlossen(veraendertesEventDAO.isAbgeschlossen());
			gefundenesEvent.setTeilnehmendeStudierende(veraendertesEventDAO.getTeilnehmendeStudierende());
			gefundenesEvent.setTeilnehmendeUnternehmen(veraendertesEventDAO.getTeilnehmendeUnternehmen());
			return iEventRepository.save(gefundenesEvent);
		} catch (NoSuchElementException e) {	//wenn findById() kein Ergebnis liefert
			System.out.println("Kein Event zu ID vorhanden!");
			return null;
		}
	}

}
