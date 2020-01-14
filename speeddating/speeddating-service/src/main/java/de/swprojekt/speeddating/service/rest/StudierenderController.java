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

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.repository.IStudierenderRepository;
/*
 * REST ist Architekturstil und bietet einheitliche Schnittstellen als Webservice an
 * Somit koennen ueber HTTP-Methoden wie GET/POST Entities abgerufen/geaendert/erstellt werden 
 */
@RestController
public class StudierenderController {
	@Autowired
	private IStudierenderRepository iStudierenderRepository;

	@GetMapping("/studierende") // Anzeige existierender Studs (http://localhost:8080/speeddating-web-7.0-SNAPSHOT/studierende/)
	public List<Studierender> getAll() {
		return iStudierenderRepository.findAll();
	}

	@GetMapping("/studierende/{id}") // suchen von einzelnem Stud
	public Studierender getOne(@PathVariable int id) {
		Optional<Studierender> gefundenerStudierender;
		try {
			gefundenerStudierender = iStudierenderRepository.findById(id);

			return gefundenerStudierender.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein Studierender zu ID vorhanden!");
			return null;
		}
	}

	@PostMapping("/studierende") // hinzufuegen von einem Stud
	public Studierender createStudierender(@RequestBody Studierender einStudierender) {
		return iStudierenderRepository.save(einStudierender);
	}

	@PutMapping("/studierende/{id}") // aendern von existierendem Stud
	public Studierender alterStudierender(@PathVariable int id, @RequestBody Studierender einStudierender) {
		Optional<Studierender> gefundenerStudierender;
		try {
			gefundenerStudierender = iStudierenderRepository.findById(id);
			gefundenerStudierender.get().setVorname(einStudierender.getVorname());
			gefundenerStudierender.get().setNachname(einStudierender.getNachname());
			gefundenerStudierender.get().setStrasse(einStudierender.getStrasse());
			gefundenerStudierender.get().setHausnummer(einStudierender.getHausnummer());
			gefundenerStudierender.get().setPlz(einStudierender.getPlz());
			gefundenerStudierender.get().setOrt(einStudierender.getOrt());
			gefundenerStudierender.get().setTelefonnr(einStudierender.getTelefonnr());
			gefundenerStudierender.get().setEmail(einStudierender.getEmail());			
			
			return iStudierenderRepository.save(gefundenerStudierender.get());
		} catch (NoSuchElementException e) {
			System.out.println("Kein Studierender zu ID vorhanden!");
			return null;
		}
	}

	@DeleteMapping("/studierende/{id}") // loeschen von existierendem Stud
	public boolean deleteStudierender(@PathVariable int id) {
		try {
			iStudierenderRepository.deleteById(id);
			return true;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Kein Studierender zu ID vorhanden!");
			return false;
		}
	}

}
