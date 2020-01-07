package de.swprojekt.speeddating.ui.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.service.pdf.IMatchingAsPDFService;
import de.swprojekt.speeddating.service.showeventorganisator.IShowEventorganisatorService;
/*
 * View fuer die Anzeige von Usern zu den Eventorganisatoren,
 * somit kann Admin diesen ihre Zugangsdaten mitteilen
 */
import de.swprojekt.speeddating.service.showuser.IShowUserService;

@Route("ui/userForAdmin")	//Erreichbar ueber Adresse: http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/userForEventorganisator
@Secured("ROLE_ADMIN")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class UserViewForAdmin extends VerticalLayout {	//VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt nebeneinander (HorizontalLayout)
	
	List<User> gefundeneEventorganisatoren=new ArrayList<User>();
	
	@Autowired	//Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public UserViewForAdmin(IShowUserService iShowUserService, IShowEventorganisatorService iShowEventorganisatorService, IMatchingAsPDFService iMatchingAsPDFService) {
	
		Grid<User> eventorganisatorenUserGrid;	//Tabelle mit Eventorganisatoren
		
		Button zugaengePDFErstellenButton=new Button("PDF fuer Zugaenge erstellen");
		Button logoutButton=new Button("Logout");
		logoutButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
	    Button zurueckButton = new Button("Zurueck");
	    zurueckButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
	    Anchor pdfLink=new Anchor(""," ");
		Label labelPassword=new Label();
		
		Notification notificationPDFsuccess = new Notification();
		notificationPDFsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelPDFsuccess = new Label("Zugaenge wurden als PDF bereitgestellt! ");
		notificationPDFsuccess.add(labelPDFsuccess);
		notificationPDFsuccess.setDuration(2500); //Meldung wird 2,5 Sekunden lang angezeigt
		
		Notification notificationFailure = new Notification();
		notificationFailure.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelFailure = new Label("PDF konnte nicht erstellt werden (Datei wird von einem anderen Prozess verwendet)! ");
		notificationFailure.add(labelFailure);
		notificationFailure.setDuration(4500); //Meldung wird 2,5 Sekunden lang angezeigt
		
		
		eventorganisatorenUserGrid = new Grid<>(User.class);	//Tabelle initialisieren
		gefundeneEventorganisatoren=iShowUserService.showEventorganisatorenUser();
		ListDataProvider<User> ldpEventorganisatorenUser = DataProvider
				.ofCollection(gefundeneEventorganisatoren);	//Dataprovider erstellen und Quelle fuer User (via Service aus DB) festlegen 
		eventorganisatorenUserGrid.setDataProvider(ldpEventorganisatorenUser);	//erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		eventorganisatorenUserGrid.removeColumnByKey("password");	//Passwort (verschluesselt) nicht in Tabelle mit anzeigen
		eventorganisatorenUserGrid.setColumns("user_id", "username");	//Spaltenordnung festlegen
		
		
		zugaengePDFErstellenButton.addClickListener(event -> {
			try {
				String password="pw*"+(new Random().nextInt((9999 - 1000) + 1) + 1000); //Schluessel zwischen 1000 und 9999 generieren
				String filename=iMatchingAsPDFService.pdfEventorganisatorenZugaengeErstellen(gefundeneEventorganisatoren, password);
				pdfLink.setHref("http://131.173.88.192:80/eventorgaZugaenge/"+filename);
				pdfLink.setText("Download als PDF");
				labelPassword.setText("BITTE NOTIEREN: Ihr Passwort zum Oeffnen der PDF: "+password);
				notificationPDFsuccess.open(); // Erfolgreich-Meldung anzeigen
			} catch (FileNotFoundException e) {
				notificationFailure.open();
				System.out.println("Bei Aufruf der PDF Erstellung gibt es Probleme ");
				e.printStackTrace();
			}
//			notificationMatchingsuccess.open();		// Erfolgreich-Meldung anzeigen	
		});
		
		
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			//getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("login"));	//zurueck auf andere Seite 
		});
		
		zurueckButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			zurueckButton.getUI().ifPresent(ui->ui.navigate("ui/eventorganisator/menue"));	//zurueck auf andere Seite 
		});
		add(new Label("EVENTORGANISATOREN"));
		add(eventorganisatorenUserGrid);	//Hinzufuegen der Elemente zum VerticalLayout
		add(zugaengePDFErstellenButton);
		add(labelPassword);
		add(pdfLink);
		add(new HorizontalLayout(zurueckButton, logoutButton));
	}
}
