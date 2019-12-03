package de.swprojekt.speeddating.service.addevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.repository.IEventRepository;
/*
 * Implementierung von IAddEventService 
 */
@Service	//Service ist erweitertes @Component
public class AddEventImpl implements IAddEventService {

	@Autowired
	IEventRepository iEventRepository;	//Interface zur Verwaltung von Events aus DB einbinden
	
	@Override
	public void speicherEvent(Event einEventDAO) {
		Event einEvent=new Event();	//neues Event-Objekt anlegen
		einEvent.setBezeichnung(einEventDAO.getBezeichnung());
		einEvent.setStartzeitpunkt(einEventDAO.getStartzeitpunkt());
		einEvent.setEndzeitpunkt(einEventDAO.getEndzeitpunkt());
		einEvent.setAbgeschlossen(einEventDAO.isAbgeschlossen());
		einEvent.setTeilnehmendeStudierende(einEventDAO.getTeilnehmendeStudierende());
		einEvent.setTeilnehmendeUnternehmen(einEventDAO.getTeilnehmendeUnternehmen());
		iEventRepository.save(einEvent);	//Speicherung in DB
	}

}
