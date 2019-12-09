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

import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.service.deleteeventorganisator.IDeleteEventorganisatorService;
/*
 * View fuer die Anzeige vorhandener Eventorganisatoren
 */
import de.swprojekt.speeddating.service.showeventorganisator.IShowEventorganisatorService;

@Route("ui/eventorganisator")	//Erreichbar ueber Adresse: http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/eventorganisator
@Secured("ROLE_ADMIN")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class EventorganisatorView extends VerticalLayout {	//VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt nebeneinander (HorizontalLayout)

	@Autowired	//Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public EventorganisatorView(IShowEventorganisatorService iShowEventorganisatorService, IDeleteEventorganisatorService iDeleteEventorganisatorService) {
	
		Grid<Eventorganisator> eventorganisatorGrid; // Tabelle mit Eventorganisatoren
		GridMultiSelectionModel<Eventorganisator> selectionModelEventorganisator;
		Button loeschenButton=new Button("Loeschen");		
		Button logoutButton=new Button("Logout");
		
		eventorganisatorGrid = new Grid<>(Eventorganisator.class); // Tabelle initialisieren
		ListDataProvider<Eventorganisator> ldpEventorganisator = DataProvider
				.ofCollection(iShowEventorganisatorService.showEventorganisatoren()); // Dataprovider erstellen und Quelle fuer
																			// Eventorganisatoren (via Service aus DB)
																			// festlegen
		eventorganisatorGrid.setDataProvider(ldpEventorganisator); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		eventorganisatorGrid.removeColumnByKey("eventorganisator_id");	//event_id nicht in Tabelle mit anzeigen
		eventorganisatorGrid.setColumns("vorname", "nachname", "fachbereich", "telefonnr", "email","verwaltet_events");	//Spaltenordnung festlegen
		eventorganisatorGrid.setSelectionMode(SelectionMode.MULTI);	//es koennen mehrere Organisatoren gleichzeitig geloescht werden
		selectionModelEventorganisator = (GridMultiSelectionModel<Eventorganisator>) eventorganisatorGrid.getSelectionModel();
		
		loeschenButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			for(Eventorganisator eo:selectionModelEventorganisator.getSelectedItems())	//markierte Events durchgehen
			{
				iDeleteEventorganisatorService.loescheEventorganisator(eo);
			}
		});
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});


		add(eventorganisatorGrid);	//Hinzufuegen der Elemente zum VerticalLayout
		add(loeschenButton);
		add(logoutButton);
	}
	//@PostConstruct	//Ausfuehrung nach Konstruktoraufruf
	//public void init()
	//{
	//	
	//}
}
