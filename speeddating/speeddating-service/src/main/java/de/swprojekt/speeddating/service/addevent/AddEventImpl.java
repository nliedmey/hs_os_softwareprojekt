package de.swprojekt.speeddating.service.addevent;

import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.repository.IEventRepository;
import de.swprojekt.speeddating.repository.IEventorganisatorRepository;
/*
 * Implementierung von IAddEventService 
 */
@Service	//Service ist erweitertes @Component
public class AddEventImpl implements IAddEventService {

	@Autowired
	IEventRepository iEventRepository;	//Interface zur Verwaltung von Events aus DB einbinden
	
	@Autowired
	IEventorganisatorRepository iEventorganisatorRepository;
	
	@Override
	public Event speicherEvent(Event einEventDAO) {
		Event einEvent=new Event();	//neues Event-Objekt anlegen
		einEvent.setBezeichnung(einEventDAO.getBezeichnung());
		einEvent.setStartzeitpunkt(einEventDAO.getStartzeitpunkt());
		einEvent.setEndzeitpunkt(einEventDAO.getEndzeitpunkt());
		einEvent.setRundendauerInMinuten(einEventDAO.getRundendauerInMinuten());
		einEvent.setAbgeschlossen(einEventDAO.isAbgeschlossen());
		einEvent.setTeilnehmendeStudierende(einEventDAO.getTeilnehmendeStudierende());
		einEvent.setTeilnehmendeUnternehmen(einEventDAO.getTeilnehmendeUnternehmen());
		return iEventRepository.save(einEvent);	//Speicherung in DB
	}
	
	public void addeEventInEventorga(int event_id, int user_id)
	{
		System.out.println("EVENTID: "+event_id+" ,USERID: "+user_id);
		try {
			Eventorganisator gefundenerEventorganisator = iEventorganisatorRepository.findById(user_id).get(); // existierendes Eventorganisatorobjekt aus DB holen
			Set<Integer> verwalteteEventsVonEventorganisator=gefundenerEventorganisator.getVerwaltet_events();
			verwalteteEventsVonEventorganisator.add(event_id);
			gefundenerEventorganisator.setVerwaltet_events(verwalteteEventsVonEventorganisator); //Eventorganisator wird Event zugeordnet (er verwaltet dieses)
			
			iEventorganisatorRepository.save(gefundenerEventorganisator);
		} catch (NoSuchElementException e) {	//wenn findById() kein Ergebnis liefert
			System.out.println("Kein Eventorganisator zu ID vorhanden!");
		}
		
	}

}
