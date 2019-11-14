package de.swprojekt.speeddating.service.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Role;
import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IRoleRepository;
import de.swprojekt.speeddating.repository.IUserRepository;
/*
 * Implementierung fuer IRegisterUserService
 */
@Service
public class RegisterUserServiceImpl implements IRegisterUserService {

	@Autowired
	private IUserRepository iUserRepository;
	
	@Autowired
	private IRoleRepository iRoleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public void save(String username, String password) {	//wird bei Registrierung ausgefuehrt
		Set<Role> roles=new HashSet<Role>(Arrays.asList(iRoleRepository.findByRolename("NORMAL"))); //standardmaessig wird ein User mit "Normal"-Rolle erstellt
		User user=new User(roles);
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password)); //eingegebenes PW verschluesseln	
		System.out.println("Jetzt wird User gespeichert "+user+", getAuth: "+user.getAuthorities()+", Roles: "+user.getRoles());
		iUserRepository.save(user);										//derzeit muss die Aenderung auf Admin noch manuell getaetigt werden, spaeter eventuell ueber Adminkonsole
	}																	
	
}
