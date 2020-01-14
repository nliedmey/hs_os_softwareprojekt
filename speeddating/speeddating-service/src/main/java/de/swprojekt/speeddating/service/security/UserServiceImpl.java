package de.swprojekt.speeddating.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IUserRepository;
/*
 * Implementierung fuer Interface UserDetailsService (aus Spring)
 */
@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private IUserRepository iUserRepository;	//User in DB verwalten

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {	//aus User in DB wird UserDetails-Objekt gebaut, welches fuer Authentifizierung genutzt wird
		User user = iUserRepository.findByUsername(username); //User aus DB laden
		if (user != null) {
			return new CustomUserDetails(user.getUsername(), user.getPassword(), true, true, true, true,
					user.getAuthorities(),user.getEntity_id_ref());	//derzeit sind enabled, accountNonExpired, credentialsNonExpired, accountNonLocked standardmaessig true
		} else {	//wenn kein User zu Username existent
			throw new UsernameNotFoundException("User: " + username + " existiert nicht!");	
		}
	}
}
