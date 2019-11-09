package de.swprojekt.speeddating.service.rest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IUserRepository;
import de.swprojekt.speeddating.service.security.IRegisterUserService;

@RestController
public class UserController {
	@Autowired
	private IUserRepository iUserRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; //ist als Bean in SecurityConfig annotiert, kann deshalb geautowired werden
	//ermoeglicht hier das PasswordHashing auch ueber einen REST-Insert via PUT
	
	@GetMapping("/user") // Anzeige existierender User (http://localhost:8080/speeddating-web-7.0-SNAPSHOT/user/)
	public List<User> getAll() {
		return iUserRepository.findAll();
	}

	@GetMapping("/user/{id}") // suchen von einzelnem User
	public User getOne(@PathVariable int id) {
		Optional<User> gefundenerUser;
		try {
			gefundenerUser = iUserRepository.findById(id);

			return gefundenerUser.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein User zu ID vorhanden!");
			return null;
		}
	}

	@PostMapping("/user") // hinzufuegen von einem User
	public User createStudierender(@RequestBody User einUser) {
		User einzufuegenerUser=new User();
		einzufuegenerUser.setUsername(einUser.getUsername());
		einzufuegenerUser.setPassword(passwordEncoder.encode(einUser.getPassword()));
		einzufuegenerUser.setRoles(einUser.getRoles());
		return iUserRepository.save(einzufuegenerUser);
	}

	@PutMapping("/user/{id}") // aendern von existierendem User
	public User alterStudierender(@PathVariable int id, @RequestBody User einUser) {
		Optional<User> gefundenerUser;	//extra get() beim Setten aufgrund von Optional-Typ benoetigt
		try {
			gefundenerUser = iUserRepository.findById(id);
			gefundenerUser.get().setUsername(einUser.getUsername());
			gefundenerUser.get().setPassword(passwordEncoder.encode(einUser.getPassword()));
//			System.out.println("PW RAW: "+einUser.getPassword()+", PW ENCODED: "+passwordEncoder.encode(einUser.getPassword()+", PW In gefUser: "+gefundenerUser.get().getPassword()));
			gefundenerUser.get().setRoles(einUser.getRoles());
			//TODO: automatisches PW-Hashing auch ueber REST-Insert via Service-Aufruf
			//return iRegisterUserService.save(gefundenerUser.get().getUsername(), gefundenerUser.get().getPassword());
			return iUserRepository.save(gefundenerUser.get());
		} catch (NoSuchElementException e) {
			System.out.println("Kein User zu ID vorhanden!");
			return null;
		}
	}

	@DeleteMapping("/user/{id}") // loeschen von existierendem User
	public boolean deleteStudierender(@PathVariable int id) {
		try {
			iUserRepository.deleteById(id);
			return true;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Kein User zu ID vorhanden!");
			return false;
		}
	}

}
