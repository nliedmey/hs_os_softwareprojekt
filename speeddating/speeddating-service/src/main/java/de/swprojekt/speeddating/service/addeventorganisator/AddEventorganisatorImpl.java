package de.swprojekt.speeddating.service.addeventorganisator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.repository.IEventorganisatorRepository;
/*
 * Implementierung von IAddEventorganisatorService 
 */
@Service	//Service ist erweitertes @Component
public class AddEventorganisatorImpl implements IAddEventorganisatorService {

	@Autowired
	IEventorganisatorRepository iEventorganisatorRepository;	//Interface zur Verwaltung von Events aus DB einbinden
	
	@Override
	public Eventorganisator speicherEventorganisator(Eventorganisator einEventorganisatorDAO) {
		Eventorganisator einEventorganisator=new Eventorganisator();
		einEventorganisator.setVorname(einEventorganisatorDAO.getVorname());
		einEventorganisator.setNachname(einEventorganisatorDAO.getNachname());
		einEventorganisator.setFachbereich(einEventorganisatorDAO.getFachbereich());
		einEventorganisator.setTelefonnr(einEventorganisatorDAO.getTelefonnr());
		einEventorganisator.setEmail(einEventorganisatorDAO.getEmail());
		einEventorganisator.setVerwaltet_events(einEventorganisatorDAO.getVerwaltet_events());
		return iEventorganisatorRepository.save(einEventorganisator); //Speicherung in DB	
	}
	
}
