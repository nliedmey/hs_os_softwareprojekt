package de.swprojekt.speeddating.ui.login;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.service.security.CustomUserDetails;

/*
 * View fuer Benutzerlogin
 */
@Route("login")
public class Login extends VerticalLayout {

	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider; // Bean aus SecurityConfig gibt Provider zurueck

	private TextField username;
	private PasswordField passwordField;
	private Button loginButton;
	private Button signupButton;

	public Login() {
		loginButton = new Button("Login");
		loginButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		signupButton = new Button("Sign Up");
		signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		username = new TextField("Username");
		passwordField = new PasswordField("Password");

		loginButton.addClickListener(event -> {
			try {
				Authentication auth = new UsernamePasswordAuthenticationToken(username.getValue(),
						passwordField.getValue());
				Authentication authenticated = daoAuthenticationProvider.authenticate(auth); // Authentifizierung ueber
																								// Username und Passwort
																								// durchfuehren
				SecurityContextHolder.getContext().setAuthentication(authenticated); // nach erfolgreicher
																						// Authentifizierung, User nun
																						// authenticated

				CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
//				for(Role r:authenticated.getAuthorities())//Schleife zur Anzeige von Roles, welche der User innehat
//				{
//					System.out.println("Role: "+r.getRole());
//				}
				Collection<? extends GrantedAuthority> authorities = authenticated.getAuthorities();
				for (GrantedAuthority gauth : authorities) {
					if (gauth.getAuthority().equals("ROLE_STUDENT")) {
						System.out.println("Referenzierte StudentenID: " + userDetails.getEntityRefId());
						loginButton.getUI().ifPresent(ui -> ui.navigate("ui/eventVotingView_Stud")); // anschliessend
																										// auf
																										// Votingseite
																										// fuer Studs
																										// weiterleiten
					} else if (gauth.getAuthority().equals("ROLE_UNTERNEHMEN")) {
						System.out.println("Referenzierte UnternehmenID: " + userDetails.getEntityRefId());
						loginButton.getUI().ifPresent(ui -> ui.navigate("ui/eventVotingView_Untern")); // anschliessend
																										// auf
																										// Votingseite
																										// fuer
																										// Unternehmen
																										// weiterleiten
					} else if (gauth.getAuthority().equals("ROLE_EVENTORGANISATOR")) {
						System.out.println("Referenzierter EventorganisatorID: " + userDetails.getEntityRefId());
						loginButton.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // anschliessend
																										// auf
																										// Votingseite
																										// fuer
																										// Unternehmen
																										// weiterleiten
					} else if (gauth.getAuthority().equals("ROLE_ADMIN")) {
						System.out.println("Hauptadmin angemeldet!");
						loginButton.getUI().ifPresent(ui -> ui.navigate("ui/admin/menue")); // anschliessend auf
																							// Votingseite fuer
																							// Unternehmen weiterleiten
					}
				}
//				System.out.println(authenticated.getAuthorities());
//				System.out.println(authenticated.getAuthorities().contains("ROLE_STUDENT"));
//				if(userDetails.getAuthorities())

			} catch (AuthenticationException e) {
				e.printStackTrace();
				Notification.show("Login failed"); // Fehlermeldung anzeigen bei fehlgeschlagenem Login (z.B. falsches
													// Passwort)
			}
			;
		});

		signupButton.addClickListener(event -> {
			signupButton.getUI().ifPresent(ui -> ui.navigate("signup")); // Weiterleitung an Registrierungsview
		});

		add(username, passwordField, loginButton, signupButton);
	}

}
