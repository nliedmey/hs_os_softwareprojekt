package de.swprojekt.speeddating.ui.login;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
/*
 * Eigene Methoden zur Einbindung in Spring-Security
 * Aufrufe in ConfigureUIServiceInitListener
 */
public class SecurityUtils {
	static boolean isUserLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	//aus SecurityContext wird der aktuelle Authentifikationsstatus geholt
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken)	//AnonymousAuthToken wird standardmaessig von Spring nicht-authentifizierten Besuchern zugewiesen
				&& authentication.isAuthenticated();
	}

	public static boolean isAccessGranted(Class<?> securedClass) {
		
		Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);	//"Secured"-Annotation kann nun zur Rollendeklaration an Views genutzt werden
		if (secured == null) { 
			System.out.println("Seite nicht geschuetzt");
			return true; //Zugriff erlauben, wenn View nicht geschuetzt (keine Rolle benoetigt)
		}
		System.out.println("Seite geschuetzt");
		// lookup needed role in user roles
		List<String> allowedRoles = Arrays.asList(secured.value());	//erlaubte Rollen fuer Aktion abrufen
		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();	//Rollen des genutzten Benutzers abrufen
		System.out.println("User Authorities: "+userAuthentication.getAuthorities()+", allowed Roles: "+allowedRoles+", userprincipal: "+userAuthentication.getPrincipal());
		return userAuthentication.getAuthorities().stream() //Ueberpruefung ob Benutzer benoetigte Rolle besitzt
				.map(GrantedAuthority::getAuthority).anyMatch(allowedRoles::contains);
	}
}
