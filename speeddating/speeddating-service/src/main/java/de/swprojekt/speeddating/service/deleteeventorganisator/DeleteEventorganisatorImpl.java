package de.swprojekt.speeddating.service.deleteeventorganisator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.repository.IEventRepository;
import de.swprojekt.speeddating.repository.IEventorganisatorRepository;
/*
 * Implementierung von IDeleteEventorganisatorService 
 */
@Service	//Service ist erweitertes @Component
public class DeleteEventorganisatorImpl implements IDeleteEventorganisatorService {

	@Autowired
	IEventorganisatorRepository iEventorganisatorRepository;	//Interface zur Verwaltung von Eventorganisatoren aus DB einbinden

	@Override
	public void loescheEventorganisator(Eventorganisator einEventorganisator) {
		iEventorganisatorRepository.delete(einEventorganisator);
	}

}
