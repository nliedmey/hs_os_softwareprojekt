package de.swprojekt.speeddating.service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
/*
 * Klasse erweitert Userklasse aus Spring
 * Dient der Umwandlung von normalem Userobjekt in UserDetails-Objekt (wird fuer Spring-Security benoetigt)
 * (z.B. loadByUsername() in UserServiceImpl)
 * 
 */
public class CustomUserDetails extends User {

	public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);	//Konstruktor von Spring-Userklasse aufrufen
	}

}
