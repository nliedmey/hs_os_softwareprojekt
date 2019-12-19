package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.grid.Grid.SelectionMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.service.deleteeventorganisator.IDeleteEventorganisatorService;
/*
 * View fuer die Anzeige vorhandener Eventorganisatoren
 */
import de.swprojekt.speeddating.service.showeventorganisator.IShowEventorganisatorService;

@Route("ui/eventorganisator") // Erreichbar ueber Adresse:
								// http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/eventorganisator
@Secured("ROLE_ADMIN") // nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch
						// bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class EventorganisatorView extends VerticalLayout { // VerticalLayout fuehrt zu Anordnung von Elementen
															// untereinander statt nebeneinander (HorizontalLayout)

	@Autowired // Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public EventorganisatorView(IShowEventorganisatorService iShowEventorganisatorService,
			IDeleteEventorganisatorService iDeleteEventorganisatorService) {

		Grid<Eventorganisator> eventorganisatorGrid; // Tabelle mit Eventorganisatoren
		GridMultiSelectionModel<Eventorganisator> selectionModelEventorganisator;
		Button loeschenButton = new Button("Loeschen");
		Button logoutButton = new Button("Logout");
		Button zurueckButton = new Button("Zurueck");

		// Bestaetigungs-Popup
		Button buttonBestaetigenJa = new Button("Ja");
		Button buttonBestaetigenNein = new Button("Nein");
		Dialog popUpBestaetigen = new Dialog();
		popUpBestaetigen.add(new Label("Den Eventorganisator endgueltig loeschen?"));
		popUpBestaetigen.add(buttonBestaetigenJa, buttonBestaetigenNein);

		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Eventorganisator erfolgreich geloescht! ");
		notificationSavesuccess.add(labelSavesuccess);
		notificationSavesuccess.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		eventorganisatorGrid = new Grid<>(Eventorganisator.class); // Tabelle initialisieren
		ListDataProvider<Eventorganisator> ldpEventorganisator = DataProvider
				.ofCollection(iShowEventorganisatorService.showEventorganisatoren()); // Dataprovider erstellen und
																						// Quelle fuer
		// Eventorganisatoren (via Service aus DB)
		// festlegen
		eventorganisatorGrid.setDataProvider(ldpEventorganisator); // erstellten Dataprovider als Datenquelle fuer
																	// Tabelle festlegen

		eventorganisatorGrid.removeColumnByKey("eventorganisator_id"); // event_id nicht in Tabelle mit anzeigen
		eventorganisatorGrid.setColumns("vorname", "nachname", "fachbereich", "telefonnr", "email", "verwaltet_events"); // Spaltenordnung
																															// festlegen
		eventorganisatorGrid.setSelectionMode(SelectionMode.MULTI); // es koennen mehrere Organisatoren gleichzeitig
																	// geloescht werden
		selectionModelEventorganisator = (GridMultiSelectionModel<Eventorganisator>) eventorganisatorGrid
				.getSelectionModel();

		loeschenButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt

			popUpBestaetigen.open();

		});

		buttonBestaetigenJa.addClickListener(event -> {
			for (Eventorganisator eo : selectionModelEventorganisator.getSelectedItems()){
				iDeleteEventorganisatorService.loescheEventorganisator(eo);
			}
			notificationSavesuccess.open(); // Erfolgreich-Meldung anzeigen
			popUpBestaetigen.close();
			loeschenButton.getUI().ifPresent(ui -> ui.navigate("ui/admin/menue")); // zurueck auf andere Seite
		});

		buttonBestaetigenNein.addClickListener(event -> {
			popUpBestaetigen.close();
		});

		logoutButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
			//getUI().get().getSession().close(); // Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});

		zurueckButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			logoutButton.getUI().ifPresent(ui -> ui.navigate("ui/admin/menue")); // zurueck auf andere Seite
		});

		add(eventorganisatorGrid); // Hinzufuegen der Elemente zum VerticalLayout
		add(loeschenButton);
		add(new HorizontalLayout(zurueckButton, logoutButton));

	}

}
