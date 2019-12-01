package de.swprojekt.speeddating.service.alterstudierender;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.repository.IStudierenderRepository;

/*
 * Implementierung des IAlterStudierenderService-Interfaces
 * Eingebunden in Views wird hingegen nur das Interface! 
 * (hierfuer muss die Implementierung als Service deklariert sein!)
 */
@Service
public class AlterStudierenderImpl implements IAlterStudierenderService {

	@Autowired // Repository wird autowired
	IStudierenderRepository iStudierenderRepository; // Zugriff auf Studierende-Entities in DB

	@Override
	public Studierender aenderStudierenden(Studierender veraenderterStudierenderDAO) {
		try {
			Studierender gefundenerStudierender = iStudierenderRepository
					.findById(veraenderterStudierenderDAO.getStudId()).get(); // existierendes Studentenobjekt aus DB holen
			gefundenerStudierender.setVorname(veraenderterStudierenderDAO.getVorname());
			gefundenerStudierender.setNachname(veraenderterStudierenderDAO.getNachname());
			gefundenerStudierender.setHauptfach(veraenderterStudierenderDAO.getHauptfach());
			return iStudierenderRepository.save(gefundenerStudierender);
		} catch (NoSuchElementException e) {	//wenn findById() kein Ergebnis liefert
			System.out.println("Kein Studierender zu ID vorhanden!");
			return null;
		}
	}

}
