package de.swprojekt.speeddating.ui.admin;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import de.swprojekt.speeddating.ui.MainLayout;
import de.swprojekt.speeddating.ui.eventorga.AddEventorganisator;
import de.swprojekt.speeddating.ui.eventorga.AlterEventorganisator;
import de.swprojekt.speeddating.ui.eventorga.EventorganisatorView;
import de.swprojekt.speeddating.ui.login.ChangePassword;
/*
 * Menue fuer Hauptadmin, wird nach Login automatisch betreten
 */
@Route(value="ui/admin/menue", layout=MainLayout.class)	//Layout wird von MainLayout uebernommen und neues aus Konstruktor hinzugefuegt
@Secured("ROLE_ADMIN")
public class AdminMenue extends Div {
	
	public AdminMenue() {
		
		Button logoutButton=new Button("Logout");

		
		VerticalLayout v1=new VerticalLayout();		
		v1.add(new Label("Admin-Menue"));
		v1.add(new RouterLink("Eventorganisatoren anzeigen/loeschen",EventorganisatorView.class));
		v1.add(new RouterLink("Eventorganisatoren hinzufuegen",AddEventorganisator.class));
		v1.add(new RouterLink("Eventorganisatoren aendern",AlterEventorganisator.class));
		v1.add(new RouterLink("Eventorganisator Zugaenge zeigen",UserViewForAdmin.class));
		v1.add("-----------");
		v1.add(new RouterLink("Mein Passwort aendern",ChangePassword.class));
		add(v1, logoutButton);
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			//getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("login"));	//zurueck auf andere Seite 
		});
	}
	
}

