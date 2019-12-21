package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import de.swprojekt.speeddating.ui.stud.StudView;
/*
 * View, welche ParentLayout (MainLayout) um Label und Link erweitert
 */
@Route(value="maincontent", layout=MainLayout.class)	//Layout wird von MainLayout uebernommen und neues aus Konstruktor hinzugefuegt
public class MainLayoutWithContent extends Div {
	
	public MainLayoutWithContent() {
		
				
		add(new Label("Hier steht dann der Inhalt..."));
		add(new RouterLink("Zeig die Studenten",StudView.class));
	}
	
}

