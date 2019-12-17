package de.swprojekt.speeddating.service.deleteeventorganisator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IEventRepository;
import de.swprojekt.speeddating.repository.IEventorganisatorRepository;
import de.swprojekt.speeddating.repository.IUserRepository;
/*
 * Implementierung von IDeleteEventorganisatorService 
 */
@Service	//Service ist erweitertes @Component
public class DeleteEventorganisatorImpl implements IDeleteEventorganisatorService {

	@Autowired
	IEventorganisatorRepository iEventorganisatorRepository;	//Interface zur Verwaltung von Eventorganisatoren aus DB einbinden

	@Autowired
	IUserRepository iUserRepository;
	
	@Override
	public void loescheEventorganisator(Eventorganisator einEventorganisatorDAO) {
		
		Eventorganisator einEventOrganisator=new Eventorganisator();		
		einEventOrganisator.setEventorganisator_id(einEventorganisatorDAO.getEventorganisator_id());
		einEventOrganisator.setNachname(einEventorganisatorDAO.getNachname());

		String username = einEventOrganisator.getNachname() + "_" + einEventOrganisator.getEventorganisator_id();
		User gefundenerUser=iUserRepository.findByUsername(username);
		if (gefundenerUser!= null) {
			iUserRepository.delete(gefundenerUser);
		}
		
		iEventorganisatorRepository.delete(einEventorganisatorDAO);
	}

}
