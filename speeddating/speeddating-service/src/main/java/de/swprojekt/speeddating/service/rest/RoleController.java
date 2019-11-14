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

import de.swprojekt.speeddating.model.Role;
import de.swprojekt.speeddating.repository.IRoleRepository;
/*
 * REST ist Architekturstil und bietet einheitliche Schnittstellen als Webservice an
 * Somit koennen ueber HTTP-Methoden wie GET/POST Entities abgerufen/geaendert/erstellt werden 
 */
@RestController
public class RoleController {
	@Autowired
	private IRoleRepository iRoleRepository;
	
	@GetMapping("/role") // Anzeige existierender Roles (http://localhost:8080/speeddating-web-7.0-SNAPSHOT/role/)
	public List<Role> getAll() {
		return iRoleRepository.findAll();
	}

	@GetMapping("/role/{id}") // suchen von einzelner Role
	public Role getOne(@PathVariable int id) {
		Optional<Role> gefundeneRole;
		try {
			gefundeneRole = iRoleRepository.findById(id);

			return gefundeneRole.get();
		} catch (NoSuchElementException e) {
			System.out.println("Keine Role zu ID vorhanden!");
			return null;
		}
	}

	@PostMapping("/role") // hinzufuegen von einer Role
	public Role createRole(@RequestBody Role eineRole) {
		Role einzufuegeneRole=new Role();
		
		einzufuegeneRole.setRole(eineRole.getRole());

		return iRoleRepository.save(einzufuegeneRole);
	}

	@PutMapping("/role/{id}") // aendern von existierender Role
	public Role alterRole(@PathVariable int id, @RequestBody Role eineRole) {
		Optional<Role> gefundeneRole;	//extra get() beim Setten aufgrund von Optional-Typ benoetigt
		try {
			gefundeneRole = iRoleRepository.findById(id);
			gefundeneRole.get().setRole(eineRole.getRole());
			return iRoleRepository.save(gefundeneRole.get());
		} catch (NoSuchElementException e) {
			System.out.println("Keine Role zu ID vorhanden!");
			return null;
		}
	}

	@DeleteMapping("/role/{id}") // loeschen von existierender Role
	public boolean deleteRole(@PathVariable int id) {
		try {
			iRoleRepository.deleteById(id);
			return true;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Keine Role zu ID vorhanden!");
			return false;
		}
	}

}
