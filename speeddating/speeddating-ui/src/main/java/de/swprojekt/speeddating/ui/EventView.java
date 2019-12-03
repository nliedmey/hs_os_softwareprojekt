package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.Grid.SelectionMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.deleteevent.IDeleteEventService;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
/*
 * View fuer die Anzeige vorhandener Events
 */

@Route("ui/event")	//Erreichbar ueber Adresse: http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/events
//@Secured("ROLE_ADMIN")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class EventView extends VerticalLayout {	//VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt nebeneinander (HorizontalLayout)

	@Autowired	//Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public EventView(IShowEventService iShowEventService, IDeleteEventService iDeleteEventService) {
	
		Grid<Event> eventGrid;	//Tabelle mit Events
		GridMultiSelectionModel<Event> selectionModelEvent;
		Button loeschenButton=new Button("Loeschen");		
		Button logoutButton=new Button("Logout");
		
		eventGrid = new Grid<>(Event.class);	//Tabelle initialisieren
		ListDataProvider<Event> ldpEvent = DataProvider
				.ofCollection(iShowEventService.showEvents());	//Dataprovider erstellen und Quelle fuer Events (via Service aus DB) festlegen 
		eventGrid.setDataProvider(ldpEvent);	//erstellten Dataprovider als Datenquelle fuer Tabelle festlegen
		
		eventGrid.removeColumnByKey("event_id");	//event_id nicht in Tabelle mit anzeigen
		eventGrid.setColumns("bezeichnung", "startzeitpunkt", "endzeitpunkt", "abgeschlossen", "teilnehmendeStudierende", "teilnehmendeUnternehmen");	//Spaltenordnung festlegen
		eventGrid.setSelectionMode(SelectionMode.MULTI);	//es koennen mehrere Events ausgewaehlt sein
		selectionModelEvent = (GridMultiSelectionModel<Event>) eventGrid.getSelectionModel();
		
		loeschenButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			for(Event e:selectionModelEvent.getSelectedItems())	//markierte Events durchgehen
			{
				iDeleteEventService.loescheEvent(e);
			}
		});
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});


		add(eventGrid);	//Hinzufuegen der Elemente zum VerticalLayout
		add(loeschenButton);
		add(logoutButton);
	}
	//@PostConstruct	//Ausfuehrung nach Konstruktoraufruf
	//public void init()
	//{
	//	
	//}
}
