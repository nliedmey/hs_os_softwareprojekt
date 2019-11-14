package de.swprojekt.speeddating.service.addstudierender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.repository.IStudierenderRepository;
/*
 * Implementierung von IAddStudierenderService 
 */
@Service
public class AddStudierenderImpl implements IAddStudierenderService {

	@Autowired
	IStudierenderRepository iStudierenderRepository;	//Interface zur Verwaltung von Studierenden aus DB einbinden
	
	@Override
	public void speicherStudierenden(Studierender einStudierenderDAO) {
		Studierender einStudierender=new Studierender();	//neues Studierender-Objekt anlegen
		einStudierender.setVorname(einStudierenderDAO.getVorname());	//Attribute aus DAO-Objekt uebernehmen 
		einStudierender.setNachname(einStudierenderDAO.getNachname());
		einStudierender.setHauptfach(einStudierenderDAO.getHauptfach());
		iStudierenderRepository.save(einStudierender);	//Speicherung in DB
	}

}
