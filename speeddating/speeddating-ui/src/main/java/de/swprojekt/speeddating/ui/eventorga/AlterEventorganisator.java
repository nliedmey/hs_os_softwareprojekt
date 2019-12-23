package de.swprojekt.speeddating.ui.eventorga;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.service.altereventorganisator.IAlterEventorganisatorService;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showeventorganisator.IShowEventorganisatorService;
import de.swprojekt.speeddating.ui.MainLayout;

@Route(value = "ui/eventorganisator/alter", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
@Secured("ROLE_ADMIN")
public class AlterEventorganisator extends VerticalLayout {
	@Autowired // Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public AlterEventorganisator(IShowEventorganisatorService iShowEventorganisatorService,
			IAlterEventorganisatorService iAlterEventorganisatorService, IShowEventService iShowEventService) {

		Binder<Eventorganisator> binder; // verknuepft Input aus Textfeldern mit Objektattributen

		Grid<Eventorganisator> eventorganisatorGrid; // Tabelle mit Eventorganisatoren
		GridSingleSelectionModel<Eventorganisator> selectionModelEventorganisator;

		Button aendernButton = new Button("Aendern");
		Button logoutButton = new Button("Logout");
		Button zurueckButton = new Button("Zurueck");
		zurueckButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

		TextField textfieldVorname = new TextField("Vorname:");
		TextField textfieldNachname = new TextField("Nachname:");
		TextField textfieldFachbereich = new TextField("Fachbereich:");
		TextField textfieldTelefonnr = new TextField("Telefonnr:");
		TextField textfieldEmail = new TextField("Email:");

		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Eventorganisator erfolgreich aktualisiert! ");
		notificationSavesuccess.add(labelSavesuccess);
		notificationSavesuccess.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt
		
		Notification notificationAbbruch = new Notification();
		notificationAbbruch.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelAbbruchsuccess = new Label("Studentbearbeitung abgebrochen! ");
		notificationAbbruch.add(labelAbbruchsuccess);
		notificationAbbruch.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		Grid<Event> eventGrid; // Tabelle mit Events, welcher Eventorganisator verwaltet
		GridMultiSelectionModel<Event> selectionModelEvent;

		eventorganisatorGrid = new Grid<>(Eventorganisator.class); // Tabelle initialisieren

		List<Eventorganisator> listTmp = iShowEventorganisatorService.showEventorganisatoren();
		List<Eventorganisator> eventorganisatorlist = new ArrayList<Eventorganisator>();

		for (Eventorganisator aEventorganisator : listTmp) {
			aEventorganisator.setAnzahlVerwalteteEvents(aEventorganisator.getVerwaltet_events().size());
			eventorganisatorlist.add(aEventorganisator);
		}

		ListDataProvider<Eventorganisator> ldpEventorganisator = DataProvider.ofCollection(eventorganisatorlist); // Dataprovider
																													// erstellen
																													// und
																													// Quelle
																													// fuer
		// Eventorganisatoren (via Service aus DB)
		// festlegen
		eventorganisatorGrid.setDataProvider(ldpEventorganisator); // erstellten Dataprovider als Datenquelle fuer
																	// Tabelle festlegen

		eventorganisatorGrid.removeColumnByKey("eventorganisator_id"); // event_id nicht in Tabelle mit anzeigen
		eventorganisatorGrid.removeColumnByKey("verwaltet_events");
		eventorganisatorGrid.setColumns("vorname", "nachname", "fachbereich", "telefonnr", "email",
				"anzahlVerwalteteEvents"); // Spaltenordnung
											// festlegen
		eventorganisatorGrid.setSelectionMode(SelectionMode.SINGLE); // es kann immer nur ein Event gleichzeitig
																		// bearbeitet werden
		selectionModelEventorganisator = (GridSingleSelectionModel<Eventorganisator>) eventorganisatorGrid
				.getSelectionModel();

		eventGrid = new Grid<>(Event.class); // Tabelle initialisieren

		List<Event> listTmp2 = iShowEventService.showEvents();
		List<Event> eventlist = new ArrayList<Event>();

		for (Event aEvent : listTmp2) {
			aEvent.setAnzahlTeilnehmendeStudierende(aEvent.getTeilnehmendeStudierende().size());
			aEvent.setAnzahlTeilnehmendeUnternehmen(aEvent.getTeilnehmendeUnternehmen().size());
			eventlist.add(aEvent);
		}
		ListDataProvider<Event> ldpEvent = DataProvider.ofCollection(eventlist); //
		eventGrid.setDataProvider(ldpEvent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		eventGrid.removeColumnByKey("event_id"); // studId nicht in Tabelle mit anzeigen
		eventGrid.removeColumnByKey("teilnehmendeStudierende");
		eventGrid.removeColumnByKey("teilnehmendeUnternehmen");

		eventGrid.setColumns("bezeichnung", "startzeitpunkt", "endzeitpunkt", "abgeschlossen",
				"anzahlTeilnehmendeStudierende", "anzahlTeilnehmendeUnternehmen"); // Spaltenordnung festlegen

		eventGrid.setSelectionMode(SelectionMode.MULTI); // es koennen mehrere Events ausgewaehlt sein
		eventGrid.setEnabled(false);
		selectionModelEvent = (GridMultiSelectionModel<Event>) eventGrid.getSelectionModel();

		eventorganisatorGrid.addSelectionListener(event -> {
			if (!selectionModelEventorganisator.getFirstSelectedItem().isEmpty()) {
				Optional<Eventorganisator> selectedEventorganisator = selectionModelEventorganisator
						.getFirstSelectedItem();
				System.out.println("abc");
				Eventorganisator zuAenderndernderEventorganisator = iShowEventorganisatorService
						.showEventorganisator(selectedEventorganisator.get().getEventorganisator_id());
				textfieldVorname.setValue(zuAenderndernderEventorganisator.getVorname());
				textfieldNachname.setValue(zuAenderndernderEventorganisator.getNachname());
				textfieldFachbereich.setValue(zuAenderndernderEventorganisator.getFachbereich());
				textfieldTelefonnr.setValue(zuAenderndernderEventorganisator.getTelefonnr());
				textfieldEmail.setValue(zuAenderndernderEventorganisator.getEmail());
				Collection<Integer> listEventsVonUnveraendertemEventorganisator = new ArrayList<>(
						iShowEventorganisatorService
								.showEventorganisator(zuAenderndernderEventorganisator.getEventorganisator_id())
								.getVerwaltet_events());

				eventGrid.deselectAll();

				for (Event e : ldpEvent.getItems()) {
					if (listEventsVonUnveraendertemEventorganisator.contains(e.getEvent_id())) // wenn Eventorganisator
																								// Event verwaltet
					{
						eventGrid.select(e); // verwaltete Events von Eventorganisator in Tabelle markieren
					}
				}
			}
		});

		Eventorganisator eventorgatmp = new Eventorganisator();
		binder = new Binder<>(Eventorganisator.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)

		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		binder.forField(textfieldVorname).asRequired("Vorname darf nicht leer sein...").bind("vorname");
		binder.forField(textfieldNachname).asRequired("Nachname darf nicht leer sein...").bind("nachname");
		binder.forField(textfieldFachbereich).asRequired("Fachbereich darf nicht leer sein...").bind("fachbereich");
		binder.forField(textfieldTelefonnr).asRequired("Telefonnr darf nicht leer sein...").bind("telefonnr");
		binder.forField(textfieldEmail).asRequired("Email darf nicht leer sein...").bind("email");
		// binder.forField(textfieldHausnummer).withConverter(new
		// StringToIntegerConverter("Eingabe muss numerisch sein")).bind("hausnummer");

		aendernButton.addClickListener(event -> {
			try {
				binder.writeBean(eventorgatmp);
				Optional<Eventorganisator> selectedEventorganisator = selectionModelEventorganisator
						.getFirstSelectedItem();
				Eventorganisator veraenderterEventorganisatorDAO = iShowEventorganisatorService
						.showEventorganisator(selectedEventorganisator.get().getEventorganisator_id());
				if (!textfieldVorname.getValue().equals(veraenderterEventorganisatorDAO.getVorname())) {
					veraenderterEventorganisatorDAO.setVorname(textfieldVorname.getValue());
				}
				if (!textfieldNachname.getValue().equals(veraenderterEventorganisatorDAO.getNachname())) {
					veraenderterEventorganisatorDAO.setNachname(textfieldNachname.getValue());
				}
				if (!textfieldFachbereich.getValue().equals(veraenderterEventorganisatorDAO.getFachbereich())) {
					veraenderterEventorganisatorDAO.setFachbereich(textfieldFachbereich.getValue());
				}
				if (!textfieldTelefonnr.getValue().equals(veraenderterEventorganisatorDAO.getTelefonnr())) {
					veraenderterEventorganisatorDAO.setTelefonnr(textfieldTelefonnr.getValue());
				}
				if (!textfieldEmail.getValue().equals(veraenderterEventorganisatorDAO.getEmail())) {
					veraenderterEventorganisatorDAO.setEmail(textfieldEmail.getValue());
				}
				Set<Integer> eventsVonUnveraendertemEventorganisator = new HashSet<>(
						veraenderterEventorganisatorDAO.getVerwaltet_events());
				Set<Integer> eventsVonVeraendertemEventorganisator = new HashSet<>();
				for (Event einAusgewaehltesEvent : selectionModelEvent.getSelectedItems()) {
					eventsVonVeraendertemEventorganisator.add(einAusgewaehltesEvent.getEvent_id());
				}
				if (!eventsVonUnveraendertemEventorganisator.equals(eventsVonVeraendertemEventorganisator)) {
					System.out.println("Eventzuordnung zu Eventorganisator veraendert!");
					veraenderterEventorganisatorDAO.setVerwaltet_events(eventsVonVeraendertemEventorganisator);
				}

				iAlterEventorganisatorService.aenderEventorganisator(veraenderterEventorganisatorDAO);
				notificationSavesuccess.open(); // Erfolgreich-Meldung anzeigen
				aendernButton.getUI().ifPresent(ui -> ui.navigate("ui/admin/menue")); // zurueck auf andere Seite

			} catch (ValidationException e) {
				System.out.println("Musseingaben wurden verletzt!");
				e.printStackTrace();
			}
		});

		logoutButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
			// getUI().get().getSession().close(); // Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});

		zurueckButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			notificationAbbruch.open();
			logoutButton.getUI().ifPresent(ui -> ui.navigate("ui/admin/menue")); // zurueck auf andere Seite
		});

		VerticalLayout v1 = new VerticalLayout(); // Textfelder sollen untereinander angeordnet werden
		v1.add(eventorganisatorGrid);
		v1.add(textfieldVorname);
		v1.add(textfieldNachname);
		v1.add(textfieldFachbereich);
		v1.add(textfieldTelefonnr);
		v1.add(textfieldEmail);
		v1.add(eventGrid);
		v1.add(aendernButton);
		v1.add(new HorizontalLayout(zurueckButton, logoutButton));
		add(v1);

	}
}
