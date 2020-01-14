package de.swprojekt.speeddating.ui.event;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.Grid.SelectionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.service.altereventorganisator.IAlterEventorganisatorService;
import de.swprojekt.speeddating.service.deleteevent.IDeleteEventService;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
/*
 * View fuer die Anzeige vorhandener Events
 */
import de.swprojekt.speeddating.service.showeventorganisator.IShowEventorganisatorService;

@Route("ui/events/show")
@Secured("ROLE_ADMIN")
public class EventView extends VerticalLayout { // VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt
												// nebeneinander (HorizontalLayout)

	@Autowired // Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public EventView(IShowEventService iShowEventService, IDeleteEventService iDeleteEventService, IShowEventorganisatorService iShowEventorganisatorService, IAlterEventorganisatorService iAlterEventorganisatorService) {

		Grid<Event> eventGrid; // Tabelle mit Events
		GridMultiSelectionModel<Event> selectionModelEvent;
		Button loeschenButton = new Button("Loeschen");
		Button logoutButton = new Button("Logout");

		List<Event> listTmp = iShowEventService.showEvents();
		List<Event> eventlist = new ArrayList<Event>();

		for (Event aEvent : listTmp) {
			aEvent.setAnzahlTeilnehmendeStudierende(aEvent.getTeilnehmendeStudierende().size());
			aEvent.setAnzahlTeilnehmendeUnternehmen(aEvent.getTeilnehmendeUnternehmen().size());
			eventlist.add(aEvent);
		}

		eventGrid = new Grid<>(Event.class); // Tabelle initialisieren
		// Dataprovider erstellen und Quelle fuer Events (via Service aus DB) festlegen
		ListDataProvider<Event> ldpEvent = DataProvider.ofCollection(eventlist);
//		ListDataProvider<Event> ldpEvent = DataProvider.ofCollection(iShowEventService.showEvents());
		eventGrid.setDataProvider(ldpEvent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		eventGrid.removeColumnByKey("event_id"); // event_id nicht in Tabelle mit anzeigen
		eventGrid.removeColumnByKey("teilnehmendeUnternehmen");
		eventGrid.removeColumnByKey("teilnehmendeStudierende");
//		eventGrid.setColumns("bezeichnung", "startzeitpunkt", "endzeitpunkt", "abgeschlossen", "teilnehmendeStudierende", "teilnehmendeUnternehmen");	//Spaltenordnung festlegen
		eventGrid.setSelectionMode(SelectionMode.MULTI); // es koennen mehrere Events ausgewaehlt sein
		selectionModelEvent = (GridMultiSelectionModel<Event>) eventGrid.getSelectionModel();

		loeschenButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			for (Event e : selectionModelEvent.getSelectedItems()) // markierte Events durchgehen
			{
				iDeleteEventService.loescheEvent(e);
				for(Eventorganisator eo:iShowEventorganisatorService.showEventorganisatoren())
				{
					System.out.println("Check organisator: "+eo.getEventorganisator_id()+"-"+eo.getNachname());
					if(eo.getVerwaltet_events().contains(e.getEvent_id()))
					{
						System.out.println("EO gefunden: "+eo.getNachname());
						Set<Integer> verwalteteEventsVorLoeschung=eo.getVerwaltet_events();
						verwalteteEventsVorLoeschung.remove(e.getEvent_id());
						System.out.println("Vor loeschung: "+verwalteteEventsVorLoeschung);
						Set<Integer> verwalteteEventsNachLoeschung=verwalteteEventsVorLoeschung;
						eo.setVerwaltet_events(verwalteteEventsNachLoeschung);
						System.out.println("Nach loeschung: "+verwalteteEventsNachLoeschung);
						iAlterEventorganisatorService.aenderEventorganisator(eo);
					}
				}
			}
		});

		logoutButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
			// getUI().get().getSession().close(); //Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});

		add(eventGrid); // Hinzufuegen der Elemente zum VerticalLayout
		add(loeschenButton);
		add(logoutButton);
	}
	// @PostConstruct //Ausfuehrung nach Konstruktoraufruf
	// public void init()
	// {
	//
	// }
}
