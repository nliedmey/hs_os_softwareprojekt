package de.swprojekt.speeddating.service.showstudierender;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.repository.IStudierenderRepository;
/*
 * Implementierung des IShowStudierendeService-Interfaces
 * Eingebunden in Views wird hingegen nur das Interface! 
 * (hierfuer muss die Implementierung als Service deklariert sein!)
 */
@Service
public class ShowStudierendeImpl implements IShowStudierendeService {

	@Autowired	//Repository wird autowired
	IStudierenderRepository iStudierenderRepository;	//Zugriff auf Studierende-Entities in DB
	
	@Override
	public List<Studierender> showStudierende() {
		return iStudierenderRepository.findAll();	//gibt Liste aller Studierender zurueck
	}

	@Override
	public Studierender showStudierenden(int stud_id) {
		Optional<Studierender> gefundenerStudierender;
		try {
			gefundenerStudierender = iStudierenderRepository.findById(stud_id);
			return gefundenerStudierender.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein Studierender zu ID vorhanden!");
			return null;
		}
	}

}
