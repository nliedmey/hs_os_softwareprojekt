package de.swprojekt.speeddating.ui;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
/*
 * Menue fuer Eventorganisatoren, wird nach Login automatisch betreten
 */
@Route(value="ui/eventorganisator/menue", layout=MainLayout.class)	//Layout wird von MainLayout uebernommen und neues aus Konstruktor hinzugefuegt
@Secured("ROLE_EVENTORGANISATOR")
public class EventorganisatorMenue extends Div {
	
	public EventorganisatorMenue() {
		
		Button logoutButton=new Button("Logout");
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("login"));	//zurueck auf andere Seite 
		});	
		
		VerticalLayout v1=new VerticalLayout();		
		v1.add(new Label("Eventorganisator-Menue"));
		v1.add(new RouterLink("Events anzeigen/loeschen",EventViewForEventorganisator.class));
		v1.add(new RouterLink("Event hinzufuegen",AddEvent.class));
		v1.add(new RouterLink("Events aendern",AlterEventForEventorganisator.class));
		v1.add("-----------");
		v1.add(new RouterLink("Studenten anzeigen",StudView.class));
		v1.add(new RouterLink("Studenten hinzufuegen",AddStud.class));
		v1.add(new RouterLink("Studenten aendern/loeschen",ChangeDeleteStud.class));
		v1.add("-----------");
		v1.add(new RouterLink("Unternehmen anzeigen",UnternView.class));
		v1.add(new RouterLink("Unternehmen hinzufuegen",AddUntern.class));
		v1.add(new RouterLink("Unternehmen aendern/loeschen",ChangeDeleteUntern.class));
		v1.add("-----------");
		v1.add(new RouterLink("Mein Passwort aendern",ChangePassword.class));
		v1.add(logoutButton);
		add(v1);
		
	}
	
}

