package de.swprojekt.speeddating.service.altereventorganisator;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.repository.IEventRepository;
import de.swprojekt.speeddating.repository.IEventorganisatorRepository;

/*
 * Implementierung des IAlterEventorganisatorService-Interfaces
 * Eingebunden in Views wird hingegen nur das Interface! 
 * (hierfuer muss die Implementierung als Service deklariert sein!)
 */
@Service
public class AlterEventorganisatorImpl implements IAlterEventorganisatorService {

	@Autowired // Repository wird autowired
	IEventorganisatorRepository iEventorganisatorRepository; // Zugriff auf Studierende-Entities in DB

	@Override
	public Eventorganisator aenderEventorganisator(Eventorganisator veraenderterEventorganisatorDAO) {
		try {
			Eventorganisator gefundenerEventorganisator = iEventorganisatorRepository
					.findById(veraenderterEventorganisatorDAO.getEventorganisator_id()).get(); // existierendes Eventorganisatorobjekt aus DB holen
			gefundenerEventorganisator.setVorname(veraenderterEventorganisatorDAO.getVorname());
			gefundenerEventorganisator.setNachname(veraenderterEventorganisatorDAO.getNachname());
			gefundenerEventorganisator.setFachbereich(veraenderterEventorganisatorDAO.getFachbereich());
			gefundenerEventorganisator.setTelefonnr(veraenderterEventorganisatorDAO.getTelefonnr());
			gefundenerEventorganisator.setEmail(veraenderterEventorganisatorDAO.getEmail());
			gefundenerEventorganisator.setVerwaltet_events(veraenderterEventorganisatorDAO.getVerwaltet_events());
			return iEventorganisatorRepository.save(gefundenerEventorganisator);
		} catch (NoSuchElementException e) {	//wenn findById() kein Ergebnis liefert
			System.out.println("Kein Eventorganisator zu ID vorhanden!");
			return null;
		}
	}

}
