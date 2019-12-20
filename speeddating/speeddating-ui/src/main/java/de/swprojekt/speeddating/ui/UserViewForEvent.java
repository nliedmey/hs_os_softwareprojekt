package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
/*
 * View fuer die Anzeige von Usern je Event
 */
import de.swprojekt.speeddating.service.showuser.IShowUserService;

@Route("ui/user")	//Erreichbar ueber Adresse: http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/user
@Secured("ROLE_EVENTORGANISATOR")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class UserViewForEvent extends VerticalLayout {	//VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt nebeneinander (HorizontalLayout)
	
	@Autowired	//Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public UserViewForEvent(IShowUserService iShowUserService, IShowEventService iShowEventService) {
	
		Grid<User> unternehmenUserGrid;	//Tabelle mit Unternehmens-Usern
		Grid<User> studierenderUserGrid;	//Tabelle mit Studierender-Usern
		
		Button logoutButton=new Button("Logout");
	    Button zurueckButton = new Button("Zurueck");
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
		studierenderUserGrid = new Grid<>(User.class);	//Tabelle initialisieren
		
//		List<Integer> gefundeneUnternehmenUserIDZuEvent=iShowUserService.showUnternehmenUserInEvent(comboboxEvent.getValue().getEvent_id()); //Unternehmens-User zu einem Event finden
//		List<User> gefundeneUnternehmenUserZuEvent=new ArrayList<User>(); 
//		for(int unternehmenUserId:gefundeneUnternehmenUserIDZuEvent)
//		{
//			gefundeneUnternehmenUserZuEvent.add(iShowUserService.showUser(unternehmenUserId));
//		}
		
//		List<Integer> gefundeneStudierendeUserIDZuEvent=iShowUserService.showStudierendeUserInEvent(comboboxEvent.getValue().getEvent_id()); //Studierende-User zu einem Event finden
//		List<User> gefundeneStudierendeUserZuEvent=new ArrayList<User>(); 
//		for(int studierendeUserId:gefundeneStudierendeUserIDZuEvent)
//		{
//			gefundeneStudierendeUserZuEvent.add(iShowUserService.showUser(studierendeUserId));
//		}
		
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
		add(new HorizontalLayout(zurueckButton, logoutButton));
	}
	//@PostConstruct	//Ausfuehrung nach Konstruktoraufruf
	//public void init()
	//{
	//	
	//}
}
