package de.swprojekt.speeddating.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
	static boolean isUserLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken)	//AnonymousAuthToken wird standardmaessig von Spring nicht-authentifizierten Besuchern zugewiesen
				&& authentication.isAuthenticated();
	}

	public static boolean isAccessGranted(Class<?> securedClass) {
		// Allow if no roles are required.
		Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);	//"Secured"-Annotation kann nun zur Rollendeklaration an Views genutzt werden
		if (secured == null) {
			return true; //Zugriff erlauben, wenn View nicht geschuetzt
		}

		// lookup needed role in user roles
		List<String> allowedRoles = Arrays.asList(secured.value());
		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("User Authorities: "+userAuthentication.getAuthorities()+", allowed Roles: "+allowedRoles+", userprincipal: "+userAuthentication.getPrincipal());
		return userAuthentication.getAuthorities().stream() //alle Authorities von User durchgehen und Berechtigung checken
				.map(GrantedAuthority::getAuthority).anyMatch(allowedRoles::contains);
	}
}
