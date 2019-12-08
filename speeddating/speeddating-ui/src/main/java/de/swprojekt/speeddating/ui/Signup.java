package de.swprojekt.speeddating.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.service.security.IRegisterUserService;
/*
 * View fuer die eigenstaendige Registrierung von Benutzern
 */
@Route("signup")
public class Signup extends VerticalLayout {

	private TextField username;
	private PasswordField passwordField;	//PasswordField ermoeglicht Verdeckung von Eingabe
	private PasswordField passwordAgainField;
	private Button saveButton;

	@Autowired
	public Signup(IRegisterUserService iRegisterUserService) {
		username = new TextField("Username");
		passwordField = new PasswordField("Passwort");
		passwordAgainField = new PasswordField("Passwort wiederholen");
		saveButton = new Button("Save");
		saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);	//Design von Button anpassen

		saveButton.addClickListener(event -> {
			if (passwordField.getValue().equals(passwordAgainField.getValue())) {	//wenn Passwoerter uebereinstimmen
				//iRegisterUserService.save(username.getValue(), passwordField.getValue(),"NORMAL",);	//speichern von User via Service
				saveButton.getUI().ifPresent(ui->ui.navigate("login"));	//anschliessend auf Loginpage weiterleiten
			} else {	//Passwoerter stimmen nicht ueberein
				Notification.show("Passwoerter stimmen nicht ueberein!");
			}

		});

		add(username, passwordField, passwordAgainField, saveButton);	//Seitenelemente hinzufuegen
	}
}
