package de.swprojekt.speeddating.ui;

import javax.validation.ValidationException;

import org.apache.commons.io.filefilter.NotFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.security.IRegisterUserService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;
import de.swprojekt.speeddating.service.unternehmen.IUnternehmenService;

/*
 * View zum Aendern von aktuellen Passwort
 */
@Route(value = "ui/changepw", layout = MainLayout.class)
@Secured({ "ROLE_EVENTORGANISATOR", "ROLE_STUDENT", "ROLE_UNTERNEHMEN", "ROLE_ADMIN" }) // Es muss ein eingeloggter User
																						// die Seite aufrufen
public class ChangePassword extends VerticalLayout {

	@Autowired
	public ChangePassword(IRegisterUserService iRegisterUserService) {

		// Deklaration
		Binder<Unternehmen> binder;

		// Erzeugen der Input Felder
		PasswordField passwordfieldAltesPasswort = new PasswordField("Altes Passwort:");
		PasswordField passwordfieldNeuesPasswort = new PasswordField("Neues Passwort:");
		PasswordField passwordfieldNeuesPasswortWdh = new PasswordField("Neues Passwort (Wdh):");

		// Button hinzufuegen
		Button buttonAendern = new Button("Passwort aendern");
		buttonAendern.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		Button buttonAbbrechen = new Button("Abbrechen");
		buttonAbbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

		// Notification
		Notification notificationAlterPwsuccess = new Notification();
		notificationAlterPwsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelAlterPwsuccess = new Label("Passwort erfolgreich geaendert!");
		notificationAlterPwsuccess.add(labelAlterPwsuccess);
		notificationAlterPwsuccess.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		Notification notificationAlterPwerror = new Notification();
		notificationAlterPwerror.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelAlterPwerror = new Label("Passwoerter stimmen nicht ueberein!");
		notificationAlterPwerror.add(labelAlterPwerror);
		notificationAlterPwerror.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		VerticalLayout h1 = new VerticalLayout();
		h1.add(passwordfieldAltesPasswort);
		h1.add(passwordfieldNeuesPasswort);
		h1.add(passwordfieldNeuesPasswortWdh);

		add(h1, buttonAendern, buttonAbbrechen);

		buttonAendern.addClickListener(event -> {
			try {
				if (passwordfieldNeuesPasswort.getValue().equals(passwordfieldNeuesPasswortWdh.getValue())) // wenn
																											// Eingaben
																											// fuer
																											// neues
																											// Passwort
																											// uebereinstimmen
				{
					CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
							.getAuthentication().getPrincipal(); // Usernamen des eingeloggten Nutzers holen
					boolean aendernErfolgreich = iRegisterUserService.changePassword(userDetails.getUsername(),
							passwordfieldAltesPasswort.getValue(), passwordfieldNeuesPasswort.getValue());
					if (aendernErfolgreich) {
						notificationAlterPwsuccess.open();

						for (GrantedAuthority gauth : userDetails.getAuthorities()) {
							if (gauth.getAuthority().equals("ROLE_STUDENT")) {
								buttonAendern.getUI().ifPresent(ui -> ui.navigate("ui/eventVotingView_Stud"));
							} else if (gauth.getAuthority().equals("ROLE_UNTERNEHMEN")) {

								buttonAendern.getUI().ifPresent(ui -> ui.navigate("ui/eventVotingView_Untern"));
							} else if (gauth.getAuthority().equals("ROLE_EVENTORGANISATOR")) {

								buttonAendern.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue"));
							} else if (gauth.getAuthority().equals("ROLE_ADMIN")) {

								buttonAendern.getUI().ifPresent(ui -> ui.navigate("ui/admin/menue"));
							}
						}
						// nachfolgendes wuerde zum sofortigen Logout ohne Meldungsanzeige fuehren
						// SecurityContextHolder.clearContext(); //Spring-Security-Session leeren
						// getUI().get().getSession().close(); //Vaadin Session leeren
					} else {
						System.out.println("Aenderung nicht vorgenommen!");
					}
				} else {
					notificationAlterPwerror.open(); // Anzeige von Fehlermeldung
				}

			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});

		buttonAbbrechen.addClickListener(event -> {

			CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal(); // Usernamen des eingeloggten Nutzers holen

			for (GrantedAuthority gauth : userDetails.getAuthorities()) {
				if (gauth.getAuthority().equals("ROLE_STUDENT")) {
					SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
//					getUI().get().getSession().close(); // Vaadin Session leeren
					buttonAbbrechen.getUI().ifPresent(ui -> ui.navigate("ui/eventVotingView_Stud"));
				} else if (gauth.getAuthority().equals("ROLE_UNTERNEHMEN")) {
					SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
//					getUI().get().getSession().close(); // Vaadin Session leeren
					buttonAbbrechen.getUI().ifPresent(ui -> ui.navigate("ui/eventVotingView_Untern"));
				} else if (gauth.getAuthority().equals("ROLE_EVENTORGANISATOR")) {
					SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
//					getUI().get().getSession().close(); // Vaadin Session leeren
					buttonAbbrechen.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue"));
				} else if (gauth.getAuthority().equals("ROLE_ADMIN")) {
					SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
//					getUI().get().getSession().close(); // Vaadin Session leeren
					buttonAbbrechen.getUI().ifPresent(ui -> ui.navigate("ui/admin/menue"));
				}
			}
		});
	}
}
