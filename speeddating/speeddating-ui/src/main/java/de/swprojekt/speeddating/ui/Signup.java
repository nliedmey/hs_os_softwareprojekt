package de.swprojekt.speeddating.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.service.security.IRegisterUserService;

@Route("signup")
public class Signup extends VerticalLayout {

	private TextField username;
	private PasswordField passwordField;
	private PasswordField passwordAgainField;
	private Button saveButton;

	@Autowired
	public Signup(IRegisterUserService iRegisterUserService) {
		username = new TextField("Username");
		passwordField = new PasswordField("Passwort");
		passwordAgainField = new PasswordField("Passwort wiederholen");
		saveButton = new Button("Save");
		saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		saveButton.addClickListener(event -> {
			if (passwordField.getValue().equals(passwordAgainField.getValue())) {
				iRegisterUserService.save(username.getValue(), passwordField.getValue());
				saveButton.getUI().ifPresent(ui->ui.navigate("login"));	//anschliessend auf Loginpage weiterleiten
			} else {	//Passwoerter stimmen nicht ueberein
				Notification.show("Passwoerter stimmen nicht ueberein!");
			}

		});

		add(username, passwordField, passwordAgainField, saveButton);
	}
}
