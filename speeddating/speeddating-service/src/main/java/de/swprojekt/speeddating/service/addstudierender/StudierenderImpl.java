package de.swprojekt.speeddating.service.addstudierender;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.repository.IStudierenderRepository;
/*
 * Implementierung von IAddStudierenderService 
 */
@Service	//Service ist erweitertes @Component
public class StudierenderImpl implements IStudierenderService {

	@Autowired
	IStudierenderRepository iStudierenderRepository;	//Interface zur Verwaltung von Studierenden aus DB einbinden
	
	@Override
	public void saveStudierenden(Studierender einStudierenderDAO) {
		Studierender einStudierender=new Studierender();	//neues Studierender-Objekt anlegen
		einStudierender.setMatrikelnummer(einStudierenderDAO.getMatrikelnummer());
		einStudierender.setVorname(einStudierenderDAO.getVorname());	//Attribute aus DAO-Objekt uebernehmen 
		einStudierender.setNachname(einStudierenderDAO.getNachname());
		einStudierender.setStrasse(einStudierenderDAO.getStrasse());
		einStudierender.setHausnummer(einStudierenderDAO.getHausnummer());
		einStudierender.setPlz(einStudierenderDAO.getPlz());
		einStudierender.setOrt(einStudierenderDAO.getOrt());
		einStudierender.setTelefonnr(einStudierenderDAO.getTelefonnr());
		einStudierender.setEmail(einStudierenderDAO.getEmail());		
		iStudierenderRepository.save(einStudierender);	//Speicherung in DB
	}
	
	public void deleteStudierenden(Studierender einStudierenderDAO) {
		
		Studierender einStudierender=new Studierender();
		einStudierender.setStudent_id(einStudierenderDAO.getStudent_id());
		iStudierenderRepository.deleteById(einStudierender.getStudent_id());
	}

	@Override
	public void changeStudierenden(Studierender einStudierenderDAO) {
		iStudierenderRepository.save(einStudierenderDAO);	//Speicherung in DB
		
	}







}
