package de.swprojekt.speeddating.ui.event;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.grid.Grid.SelectionMode;

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
import de.swprojekt.speeddating.service.deleteevent.IDeleteEventService;
import de.swprojekt.speeddating.service.pdf.IMatchingAsPDFService;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
/*
 * View fuer die Anzeige vorhandener Events
 */

@Route("ui/eventsForOrganisator") // Erreichbar ueber Adresse:
									// http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/events
@Secured("ROLE_EVENTORGANISATOR") // nur User mit Rolle EVENTORGANISATOR koennen auf Seite zugreifen, @Secured
									// prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class EventViewForEventorganisator extends VerticalLayout { // VerticalLayout fuehrt zu Anordnung von Elementen
																	// untereinander statt nebeneinander
																	// (HorizontalLayout)

	@Autowired // Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public EventViewForEventorganisator(IShowEventService iShowEventService, IDeleteEventService iDeleteEventService,
			IMatchingAsPDFService iMatchingAsPDFService) {

		Grid<Event> eventGrid; // Tabelle mit Events
		GridMultiSelectionModel<Event> selectionModelEvent;
		Button pdfButton = new Button("PDF-Download");
		Button loeschenButton = new Button("Loeschen");
		Button logoutButton = new Button("Logout");
		Button zurueckButton = new Button("Zurueck");
		Anchor pdfLink = new Anchor("", " ");
		Label labelPassword = new Label();

		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Event wurde geloescht! ");
		notificationSavesuccess.add(labelSavesuccess);
		notificationSavesuccess.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		// Notifications fuer PDF
		Notification notificationPDFerror = new Notification();
		notificationPDFerror.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelPDFerror = new Label();
		notificationPDFerror.add(labelPDFerror);
		notificationPDFerror.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		Notification notificationPDFsuccess = new Notification();
		notificationPDFsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelPDFsuccess = new Label();
		notificationPDFsuccess.add(labelPDFsuccess);
		notificationPDFsuccess.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		List<Event> listOfEvents = new ArrayList<Event>();
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal(); // Id des eingeloggten Users aus SecurityKontext holen

		// Hier auskommentieren, wenn Events nur fuer den Organisator geladen werden
		// sollen
		for (int event_id : iShowEventService.showEventsOfUser(userDetails.getEntityRefId())) // alle Events, welche von
																								// Eventorganisator
																								// verwaltet, laden
		{
			Event aEvent = iShowEventService.showEvent(event_id);
			if (aEvent != null) {
				aEvent.setAnzahlTeilnehmendeStudierende(aEvent.getTeilnehmendeStudierende().size());
				aEvent.setAnzahlTeilnehmendeUnternehmen(aEvent.getTeilnehmendeUnternehmen().size());
				listOfEvents.add(aEvent);
			}
		}
//		listOfEvents.addAll(iShowEventService.showEvents());

		eventGrid = new Grid<>(Event.class); // Tabelle initialisieren
		ListDataProvider<Event> ldpEvent = DataProvider.ofCollection(listOfEvents); // Dataprovider erstellen und Quelle
																					// fuer Events (via Service aus DB)
																					// festlegen
		eventGrid.setDataProvider(ldpEvent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		eventGrid.removeColumnByKey("event_id"); // event_id nicht in Tabelle mit anzeigen
		eventGrid.removeColumnByKey("teilnehmendeStudierende");
		eventGrid.removeColumnByKey("teilnehmendeUnternehmen");
		eventGrid.setColumns("bezeichnung", "startzeitpunkt", "endzeitpunkt", "abgeschlossen",
				"anzahlTeilnehmendeStudierende", "anzahlTeilnehmendeUnternehmen"); // Spaltenordnung festlegen
		eventGrid.setSelectionMode(SelectionMode.MULTI); // es koennen mehrere Events ausgewaehlt sein
		selectionModelEvent = (GridMultiSelectionModel<Event>) eventGrid.getSelectionModel();

		pdfButton.addClickListener(event -> {
			for (Event aEvent : selectionModelEvent.getSelectedItems()) // markierte Events durchgehen
			{
				Event selectedEvent = iShowEventService.showEvent(aEvent.getEvent_id());
				if (selectedEvent.isAbgeschlossen()) {

					// MatchingAsPDF objektForCreatingPDF = new MatchingAsPDF();
					try {
						String password = "pw*" + (new Random().nextInt((9999 - 1000) + 1) + 1000); // Schluessel
																									// zwischen 1000 und
																									// 9999 generieren

						String filename = iMatchingAsPDFService.pdfMatchingErgebnisseErstellen(
								iShowEventService.generateMatchingResultSet(selectedEvent), selectedEvent.getEvent_id(),
								selectedEvent.getBezeichnung(), password);

						// String
						// filename=objektForCreatingPDF.pdfErstellen(iShowEventService.generateMatchingResultSet(selectedEvent),selectedEvent.getEvent_id(),
						// selectedEvent.getBezeichnung(),password);
						pdfLink.setHref("http://131.173.88.192:80/matchingAuswertungen/" + filename);
						pdfLink.setText("Download als PDF");
						labelPassword.setText("BITTE NOTIEREN: Ihr Passwort zum Oeffnen der PDF: " + password);
						labelPDFsuccess.setText("PDF fuer das Event " + selectedEvent.getBezeichnung() + " erstellt!");
						notificationPDFsuccess.open();
					} catch (FileNotFoundException e) {
						System.out.println("Bei Aufruf der PDF Erstellung gibt es Probleme");
						e.printStackTrace();
					}
				} else {
					labelPDFerror
							.setText("Das Event " + selectedEvent.getBezeichnung() + " ist noch nicht abgeschlossen!");
					notificationPDFerror.open();
				}
			}
		});

		loeschenButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			for (Event e : selectionModelEvent.getSelectedItems()) // markierte Events durchgehen
			{
				iDeleteEventService.loescheEvent(e);
			}
			notificationSavesuccess.open();
			loeschenButton.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere
																								// Seite

		});

		logoutButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
			// getUI().get().getSession().close(); // Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});

		zurueckButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			zurueckButton.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere Seite
		});

		add(eventGrid); // Hinzufuegen der Elemente zum VerticalLayout
		add(pdfButton, loeschenButton);
		add(pdfLink);
		add(labelPassword);
		add(new HorizontalLayout(zurueckButton, logoutButton));
	}
	// @PostConstruct //Ausfuehrung nach Konstruktoraufruf
	// public void init()
	// {
	//
	// }
}
