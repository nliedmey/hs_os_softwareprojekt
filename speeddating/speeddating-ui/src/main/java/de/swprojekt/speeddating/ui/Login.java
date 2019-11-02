package de.swprojekt.speeddating.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("login")
public class Login extends VerticalLayout {

	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider;

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
				Authentication authenticated = daoAuthenticationProvider.authenticate(auth);
				SecurityContextHolder.getContext().setAuthentication(authenticated); // nach erfolgreicher Authentifizierung, User nun authenticated
				loginButton.getUI().ifPresent(ui->ui.navigate("ui/studs"));	//anschliessend auf Loginpage weiterleiten
				//Logout: SecurityContextHolder.clearContext();
			} catch (AuthenticationException e) {
				e.printStackTrace();
				Notification.show("Login failed");
			}
			;
		});

		signupButton.addClickListener(event -> {
			;
		});

		add(username, passwordField, loginButton, signupButton);
	}

}
