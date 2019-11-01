package de.swprojekt.speeddating.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("login")
public class Login extends VerticalLayout{

	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider;
	
	private TextField username;
	private PasswordField passwordField;
	private Button loginButton;
	private Button signupButton;
	
	public Login()
	{
		loginButton=new Button("Login");
		loginButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		signupButton=new Button("Sign Up");
		signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		username=new TextField("Username");
		passwordField=new PasswordField("Password");
		
		loginButton.addClickListener(event->{
			;
		});
		
		signupButton.addClickListener(event->{
			;
		});
		
		add(username,loginButton,signupButton);
		System.out.println(passwordField);
		add(passwordField);
	}
	
}
