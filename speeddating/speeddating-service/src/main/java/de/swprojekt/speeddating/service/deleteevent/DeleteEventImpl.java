package de.swprojekt.speeddating.service.deleteevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.repository.IEventRepository;
/*
 * Implementierung von IDeleteEventService 
 */
@Service	//Service ist erweitertes @Component
public class DeleteEventImpl implements IDeleteEventService {

	@Autowired
	IEventRepository iEventRepository;	//Interface zur Verwaltung von Events aus DB einbinden

	@Override
	public void loescheEvent(Event einEvent) {
		iEventRepository.delete(einEvent);
	}

}
