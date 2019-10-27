package de.swprojekt.speeddating.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

@Route(value="maincontent", layout=MainLayout.class)
public class MainLayoutWithContent extends Div {
	
	public MainLayoutWithContent() {
		add(new Label("Hier steht dann der Inhalt..."));
		add(new RouterLink("Zeig die Studenten",StudView.class));
	}
	
}
