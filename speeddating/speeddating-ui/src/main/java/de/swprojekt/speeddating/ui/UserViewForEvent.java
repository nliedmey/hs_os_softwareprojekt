package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.MatchingAsPDF;
import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IUserRepository;
import de.swprojekt.speeddating.service.pdf.IMatchingAsPDFService;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
/*
 * View fuer die Anzeige von Usern je Event
 */
import de.swprojekt.speeddating.service.showuser.IShowUserService;

@Route("ui/userForEventorganisator")	//Erreichbar ueber Adresse: http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/userForEventorganisator
@Secured("ROLE_EVENTORGANISATOR")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class UserViewForEvent extends VerticalLayout {	//VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt nebeneinander (HorizontalLayout)
	
	@Autowired	//Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public UserViewForEvent(IShowUserService iShowUserService, IShowEventService iShowEventService, IMatchingAsPDFService iMatchingAsPDFService) {
	
		Grid<User> unternehmenUserGrid;	//Tabelle mit Unternehmens-Usern
		Grid<User> studierenderUserGrid;	//Tabelle mit Studierender-Usern
		
		Button zugaengePDFErstellenButton=new Button("PDF fuer Zugaenge erstellen");
		Button logoutButton=new Button("Logout");
	    Button zurueckButton = new Button("Zurueck");
	    Anchor pdfLink=new Anchor(""," ");
		Label labelPassword=new Label();
		
		Notification notificationMatchingsuccess = new Notification();
		notificationMatchingsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelMatchingsuccess = new Label("Matching Ergebnisse wurden als PDF bereitgestellt! ");
		notificationMatchingsuccess.add(labelMatchingsuccess);
		notificationMatchingsuccess.setDuration(2500); //Meldung wird 2,5 Sekunden lang angezeigt
		
	    ComboBox<Event> comboboxEvent = new ComboBox<>();
		comboboxEvent.setLabel("Event auswaehlen");
		comboboxEvent.setItemLabelGenerator(Event::getBezeichnung);
		List<Event> listOfEvents = new ArrayList<Event>();
		
		CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	//Id des eingeloggten Users aus SecurityKontext holen
		
		for(int event_id:iShowEventService.showEventsOfUser(userDetails.getEntityRefId()))	//alle Events, an welchen der User beteiligt ist, laden
		{
			listOfEvents.add(iShowEventService.showEvent(event_id));
		}
		comboboxEvent.setDataProvider(new ListDataProvider<>(listOfEvents));	//Liste dient als Datanquelle fuer ComboBox
		comboboxEvent.setValue(listOfEvents.get(0)); //ein Event ist standardmaessig ausgewaehlt (i.d.R. existiert auch nur eins je Student)
		
		unternehmenUserGrid = new Grid<>(User.class);	//Tabelle initialisieren
		studierenderUserGrid = new Grid<>(User.class);	
		
		List<User> gefundeneStudierendeUserZuEvent=iShowUserService.showStudierendeUserInEvent(comboboxEvent.getValue().getEvent_id());
		List<User> gefundeneUnternehmenUserZuEvent=iShowUserService.showUnternehmenUserInEvent(comboboxEvent.getValue().getEvent_id());
		
		ListDataProvider<User> ldpUnternehmenUser = DataProvider
				.ofCollection(gefundeneUnternehmenUserZuEvent);	//Dataprovider erstellen und Quelle fuer User (via Service aus DB) festlegen 
		unternehmenUserGrid.setDataProvider(ldpUnternehmenUser);	//erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		unternehmenUserGrid.removeColumnByKey("password");	//Passwort (verschluesselt) nicht in Tabelle mit anzeigen
		unternehmenUserGrid.setColumns("user_id", "username");	//Spaltenordnung festlegen
		
		ListDataProvider<User> ldpStudierendeUser = DataProvider
				.ofCollection(gefundeneStudierendeUserZuEvent);	//Dataprovider erstellen und Quelle fuer User (via Service aus DB) festlegen 
		studierenderUserGrid.setDataProvider(ldpStudierendeUser);	//erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		studierenderUserGrid.removeColumnByKey("password");	//Passwort (verschluesselt) nicht in Tabelle mit anzeigen
		studierenderUserGrid.setColumns("user_id", "username");	//Spaltenordnung festlegen
		
		comboboxEvent.addValueChangeListener(event -> {
			Event aEvent = comboboxEvent.getValue();
			
			
			gefundeneUnternehmenUserZuEvent.clear(); //alte Eintraege clearen
			gefundeneStudierendeUserZuEvent.clear();
			
			
			if (aEvent != null) {
				System.out.println(comboboxEvent.getValue().getEvent_id());
				gefundeneStudierendeUserZuEvent.addAll(iShowUserService.showStudierendeUserInEvent(comboboxEvent.getValue().getEvent_id()));
				gefundeneUnternehmenUserZuEvent.addAll(iShowUserService.showUnternehmenUserInEvent(comboboxEvent.getValue().getEvent_id()));
		
				unternehmenUserGrid.getDataProvider().refreshAll(); //nach dem Aendern des ComboBox-Wertes werden die Tabellen aktualisiert
				studierenderUserGrid.getDataProvider().refreshAll();
				
			} else {
				System.out.println("Kein Event vorhanden");
			}
		});
		
		
		zugaengePDFErstellenButton.addClickListener(event -> {
			Event selectedEvent = comboboxEvent.getValue();

			try {
				String password="pw*"+(new Random().nextInt((9999 - 1000) + 1) + 1000); //Schluessel zwischen 1000 und 9999 generieren
				List<User> usersInEvent=iShowUserService.showStudierendeUserInEvent(comboboxEvent.getValue().getEvent_id());
				usersInEvent.addAll(iShowUserService.showUnternehmenUserInEvent(comboboxEvent.getValue().getEvent_id()));
				String filename=iMatchingAsPDFService.pdfErstellen(usersInEvent, selectedEvent.getEvent_id(), selectedEvent.getBezeichnung(), password);
				pdfLink.setHref("http://131.173.88.192:80/teilnehmerZugaenge/"+filename);
				pdfLink.setText("Download als PDF");
				labelPassword.setText("BITTE NOTIEREN: Ihr Passwort zum Oeffnen der PDF: "+password);
				notificationMatchingsuccess.open(); // Erfolgreich-Meldung anzeigen
			} catch (FileNotFoundException e) {
				System.out.println("Bei Aufruf der PDF Erstellung gibt es Probleme");
				e.printStackTrace();
			}
			notificationMatchingsuccess.open();		// Erfolgreich-Meldung anzeigen	
		});
		
		
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			//getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});
		
		zurueckButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			zurueckButton.getUI().ifPresent(ui->ui.navigate("ui/eventorganisator/menue"));	//zurueck auf andere Seite 
		});
		add(comboboxEvent);
		add(new Label("UNTERNEHMEN"));
		add(unternehmenUserGrid);	//Hinzufuegen der Elemente zum VerticalLayout
		add(new Label("STUDIERENDE"));
		add(studierenderUserGrid);
		add(zugaengePDFErstellenButton);
		add(labelPassword);
		add(pdfLink);
		add(new HorizontalLayout(zurueckButton, logoutButton));
	}
}
