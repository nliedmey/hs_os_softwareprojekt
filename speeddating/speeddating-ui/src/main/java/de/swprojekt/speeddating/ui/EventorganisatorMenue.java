package de.swprojekt.speeddating.ui;

import org.springframework.security.access.annotation.Secured;

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
		
		VerticalLayout v1=new VerticalLayout();		
		v1.add(new Label("Eventorganisator-Menue"));
		v1.add(new RouterLink("Events anzeigen/loeschen",EventView.class));
		v1.add(new RouterLink("Event hinzufuegen",AddEvent.class));
		v1.add(new RouterLink("Events aendern",AlterEvent.class));
		v1.add("-----------");
		v1.add(new RouterLink("Studenten anzeigen",StudView.class));
		v1.add(new RouterLink("Studenten hinzufuegen",AddStud.class));
		v1.add(new RouterLink("Studenten aendern/loeschen",ChangeDeleteStud.class));
		v1.add("-----------");
		v1.add(new RouterLink("Unternehmen anzeigen",UnternView.class));
		v1.add(new RouterLink("Unternehmen hinzufuegen",AddUntern.class));
		v1.add(new RouterLink("Unternehmen aendern/loeschen",ChangeDeleteUntern.class));
		
		add(v1);
	}
	
}

