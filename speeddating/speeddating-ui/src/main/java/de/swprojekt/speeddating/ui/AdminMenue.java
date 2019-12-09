package de.swprojekt.speeddating.ui;

import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
/*
 * Menue fuer Hauptadmin, wird nach Login automatisch betreten
 */
@Route(value="ui/admin/menue", layout=MainLayout.class)	//Layout wird von MainLayout uebernommen und neues aus Konstruktor hinzugefuegt
@Secured("ROLE_ADMIN")
public class AdminMenue extends Div {
	
	public AdminMenue() {
		
		VerticalLayout v1=new VerticalLayout();		
		v1.add(new Label("Admin-Menue"));
		v1.add(new RouterLink("Eventorganisatoren anzeigen/loeschen",EventorganisatorView.class));
		v1.add(new RouterLink("Eventorganisatoren hinzufuegen",AddEventorganisator.class));
		v1.add(new RouterLink("Eventorganisatoren aendern",AlterEventorganisator.class));
		
		add(v1);
	}
	
}

