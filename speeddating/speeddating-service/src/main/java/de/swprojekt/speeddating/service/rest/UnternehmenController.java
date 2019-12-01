package de.swprojekt.speeddating.service.rest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.repository.IUnternehmenRepository;

@RestController
public class UnternehmenController {

	@Autowired
	private IUnternehmenRepository iUnternehmenRepository;
	
	@GetMapping("/unternehmen") // Anzeige existierender Unternehmen (http://localhost:8080/speeddating-web-7.0-SNAPSHOT/unternehmen/)
	public List<Unternehmen> getAll() {
		return iUnternehmenRepository.findAll();
	}
	
	@GetMapping("/unternehmen/{id}") // suchen von einzelnem Unternehmen
	public Unternehmen getOne(@PathVariable int id) {
		Optional<Unternehmen> gefundenesUnternehmen;
		try {
			gefundenesUnternehmen = iUnternehmenRepository.findById(id);

			return gefundenesUnternehmen.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein Studierender zu ID vorhanden!");
			return null;
		}
	}

	@PostMapping("/unternehmen") // hinzufuegen von einem Unternehmen
	public Unternehmen createUnternehmen(@RequestBody Unternehmen einUnternehmen) {
		return iUnternehmenRepository.save(einUnternehmen);
	}

	@PutMapping("/unternehmen/{id}") // aendern von existierendem Unternehmen
	public Unternehmen altesUnternehmen(@PathVariable int id, @RequestBody Unternehmen einUnternehmen) {
		Optional<Unternehmen> gefundenesUnternehmen;
		try {
			gefundenesUnternehmen = iUnternehmenRepository.findById(id);
			gefundenesUnternehmen.get().setUnternehmensname(einUnternehmen.getUnternehmensname());
			gefundenesUnternehmen.get().setAnsprechpartner(einUnternehmen.getAnsprechpartner());
			gefundenesUnternehmen.get().setKontaktmail(einUnternehmen.getKontaktmail());			
			
			return iUnternehmenRepository.save(gefundenesUnternehmen.get());
		} catch (NoSuchElementException e) {
			System.out.println("Kein Unternehmen zu ID vorhanden!");
			return null;
		}
	}

	@DeleteMapping("/unternehmen/{id}") // loeschen von existierendem Unternehmen
	public boolean deleteUnternehmen(@PathVariable int id) {
		try {
			iUnternehmenRepository.deleteById(id);
			return true;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Kein Unternehmen zu ID vorhanden!");
			return false;
		}
	}
	
}
