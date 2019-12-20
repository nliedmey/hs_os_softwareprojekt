package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.io.FileNotFoundException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.pdf.IMatchingAsPDFService;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;

/*
 * 
 */
@Route(value = "ui/untern/EventMatchingDisplayView", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
@Secured("ROLE_EVENTORGANISATOR")
public class EventMatchingDisplayView extends HorizontalLayout {

	int lv_id = 0;
	// BestPractice: Konstruktor-Injection im Vergleich zu
	// Attribut/Methoden-Injection
	// Parameter (hier: IAddStudierenderService) wird also automatisch autowired
	Grid<Unternehmen> unternehmenGrid = new Grid<>(Unternehmen.class);
	Grid<Unternehmen> unternehmenGrid2 = new Grid<>(Unternehmen.class);

	Grid<Studierender> studierenderGrid = new Grid<>(Studierender.class);
	Grid<Studierender> studierenderGrid2 = new Grid<>(Studierender.class);

	Set<Integer> studentKontaktWuensche = new HashSet<>();
	Set<Integer> unternehmenKontaktWuensche = new HashSet<>();
	List<Studierender> listOfStudOffeneStimmen = new ArrayList<Studierender>();
	List<Studierender> listOfStudVotedStimmen = new ArrayList<Studierender>();
	List<Unternehmen> listOfUnternOffeneStimmen = new ArrayList<Unternehmen>();
	List<Unternehmen> listOfUnternVotedStimmen = new ArrayList<Unternehmen>();

	@Autowired
	public EventMatchingDisplayView(IShowEventService iShowEventService,
			IShowStudierendeService iShowStudierendeService, IShowUnternehmenService iShowUnternehmenService,
			IMatchingAsPDFService iMatchingAsPDFService) {

		Binder<Event> binder; // verknuepft Input aus Textfeldern mit Objektattributen
		DatePicker datepickerStartzeitpunktDatum = new DatePicker("Startdatum:");
		TimePicker timepickerStartzeitpunktUhrzeit = new TimePicker("Startzeit:");
		DatePicker datepickerEndzeitpunktDatum = new DatePicker("Enddatum:");
		TimePicker timepickerEndzeitpunktUhrzeit = new TimePicker("Endzeit:");
		TextField textfieldRundendauerInMinuten = new TextField("Rundendauer (min):");
		Checkbox checkboxAbgeschlossen = new Checkbox("Abgeschlossen:");
		Anchor pdfLink = new Anchor("", " ");
		Label labelPassword = new Label();

		datepickerStartzeitpunktDatum.setEnabled(false);
		timepickerStartzeitpunktUhrzeit.setEnabled(false);
		datepickerEndzeitpunktDatum.setEnabled(false);
		timepickerEndzeitpunktUhrzeit.setEnabled(false);
		textfieldRundendauerInMinuten.setEnabled(false);
		checkboxAbgeschlossen.setEnabled(false);
		// Alternative ist setReadableOnly...

		// Button #1 hinzufuegen
		Button buttonMatchingDuerchfuehren = new Button("Matching durchfuehren");
		buttonMatchingDuerchfuehren.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		// Button #2 hinzufuegen
		Button buttonZurueck = new Button("Zurueck");
		buttonZurueck.addThemeVariants(ButtonVariant.LUMO_ERROR);
		Button logoutButton = new Button("Logout");

		// Notification Meldungen mit Button verknuepfen
		Notification notificationMatchingsuccess = new Notification();
		notificationMatchingsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelMatchingsuccess = new Label("Matching Ergebnisse wurden als PDF bereitgestellt! ");
		notificationMatchingsuccess.add(labelMatchingsuccess);
		notificationMatchingsuccess.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		Notification notificationZurueck = new Notification();
		notificationZurueck.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelZuruecksuccess = new Label("Bearbeitung abgebrochen! ");
		notificationZurueck.add(labelZuruecksuccess);
		notificationZurueck.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		Notification notificationNotPossible = new Notification();
		notificationNotPossible.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelNotPossible = new Label("Offene Stimmabgaben, daher kein Matching moeglich! ");
		notificationNotPossible.add(labelNotPossible);
		notificationNotPossible.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		List<Event> listOfEvents = new ArrayList<Event>();
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal(); // Id des eingeloggten Users aus SecurityKontext holen

		for (int event_id : iShowEventService.showEventsOfUser(userDetails.getEntityRefId())) // alle Events, welche von
																								// Eventorganisator
																								// verwaltet, laden
		{
			listOfEvents.add(iShowEventService.showEvent(event_id));
		}

		// Erzeugen der Combo Box
		ComboBox<Event> comboBox = new ComboBox<>();
		comboBox.setLabel("Event auswaehlen");
		comboBox.setItemLabelGenerator(Event::getBezeichnung);
		ListDataProvider<Event> ldpEvent = DataProvider.ofCollection(listOfEvents); // Dataprovider erstellen und Quelle
																					// fuer
																					// Events (via Service aus DB)
																					// festlegen
		comboBox.setDataProvider(ldpEvent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen
		comboBox.addValueChangeListener(event -> {
			Event aEvent = comboBox.getValue();
			if (aEvent != null) {

				initialize();

				lv_id = aEvent.getEvent_id();
				Event selectedEvent = iShowEventService.showEvent(lv_id);
				// Uhrzeit und Datum aus Date herausfiltern und in date/timepicker einsetzen
				datepickerStartzeitpunktDatum.setValue(
						selectedEvent.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				timepickerStartzeitpunktUhrzeit.setValue(
						selectedEvent.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
				datepickerEndzeitpunktDatum.setValue(
						selectedEvent.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				timepickerEndzeitpunktUhrzeit.setValue(
						selectedEvent.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
				textfieldRundendauerInMinuten.setValue(String.valueOf(selectedEvent.getRundendauerInMinuten()));
				checkboxAbgeschlossen.setValue(selectedEvent.isAbgeschlossen());

				for (Integer student_id : selectedEvent.getTeilnehmendeStudierende()) {
					Studierender aStudent = iShowStudierendeService.showStudierenden(student_id);
					studentKontaktWuensche.clear();
					if (aStudent != null) {
						studentKontaktWuensche = aStudent.getStudentKontaktwuensche();
						if (studentKontaktWuensche.isEmpty()) {
							listOfStudOffeneStimmen.add(aStudent);
						} else {
							listOfStudVotedStimmen.add(aStudent);
						}
					}
				}

				for (Integer unternehmen_id : selectedEvent.getTeilnehmendeUnternehmen()) {
					Unternehmen aUnternehmen = iShowUnternehmenService.showEinUnternehmen(unternehmen_id);
					unternehmenKontaktWuensche.clear();
					if (aUnternehmen != null) {
						unternehmenKontaktWuensche = aUnternehmen.getUnternehmenKontaktwuensche();
						if (unternehmenKontaktWuensche.isEmpty()) {
							listOfUnternOffeneStimmen.add(aUnternehmen);
						} else {
							listOfUnternVotedStimmen.add(aUnternehmen);
						}
					}
				}

				ListDataProvider<Studierender> ldpStudent = DataProvider.ofCollection(listOfStudVotedStimmen);
				studierenderGrid.setDataProvider(ldpStudent);

				ListDataProvider<Studierender> ldpStudent2 = DataProvider.ofCollection(listOfStudOffeneStimmen);
				studierenderGrid2.setDataProvider(ldpStudent2);

				ListDataProvider<Unternehmen> ldpUnternehmen = DataProvider.ofCollection(listOfUnternVotedStimmen);
				unternehmenGrid.setDataProvider(ldpUnternehmen);

				ListDataProvider<Unternehmen> ldpUnternehmen2 = DataProvider.ofCollection(listOfUnternOffeneStimmen);
				unternehmenGrid2.setDataProvider(ldpUnternehmen2);

			} else {

			}
		});

		buttonZurueck.addClickListener(event -> {
			// Erfolgreich-Meldung anzeigen
			notificationZurueck.open();
			// SecurityContextHolder.clearContext(); //Spring-Security-Session leeren
			// getUI().get().getSession().close(); //Vaadin Session leeren
			buttonZurueck.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere Seite
		});

		buttonMatchingDuerchfuehren.addClickListener(event -> {
//			if (listOfUnternOffeneStimmen.isEmpty() && listOfStudOffeneStimmen.isEmpty()) {
			Event aEvent = comboBox.getValue();
			;
			Event selectedEvent = iShowEventService.showEvent(aEvent.getEvent_id());

//			MatchingAsPDF objektForCreatingPDF = new MatchingAsPDF();
			try {
				String password = "pw*" + (new Random().nextInt((9999 - 1000) + 1) + 1000); // Schluessel zwischen 1000
																							// und 9999 generieren
				String filename = iMatchingAsPDFService.pdfMatchingErgebnisseErstellen(
						iShowEventService.generateMatchingResultSet(selectedEvent), selectedEvent.getEvent_id(),
						selectedEvent.getBezeichnung(), password);
//				String filename=objektForCreatingPDF.pdfErstellen(iShowEventService.generateMatchingResultSet(selectedEvent), selectedEvent.getEvent_id(), selectedEvent.getBezeichnung(), password);	

				pdfLink.setHref("http://131.173.88.192:80/matchingAuswertungen/" + filename);
				pdfLink.setText("Download als PDF");
				labelPassword.setText("BITTE NOTIEREN: Ihr Passwort zum Oeffnen der PDF: " + password);
				notificationMatchingsuccess.open(); // Erfolgreich-Meldung anzeigen
			} catch (FileNotFoundException e) {
				System.out.println("Bei Aufruf der PDF Erstellung gibt es Probleme");
				e.printStackTrace();
			}

			// Erfolgreich-Meldung anzeigen
			notificationMatchingsuccess.open();
//			buttonMatchingDuerchfuehren.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere Seite

//			} else {
//				notificationNotPossible.open();
//			}			
		});

		logoutButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
			// getUI().get().getSession().close(); //Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});

		unternehmenGrid.removeColumnByKey("unternehmen_id");
		unternehmenGrid.setColumns("unternehmensname", "ansprechpartner");
		unternehmenGrid2.removeColumnByKey("unternehmen_id");
		unternehmenGrid2.setColumns("unternehmensname", "ansprechpartner");
		studierenderGrid.removeColumnByKey("student_id");
		studierenderGrid.setColumns("vorname", "nachname");
		studierenderGrid2.removeColumnByKey("student_id");
		studierenderGrid2.setColumns("vorname", "nachname");

		// *** Erzeugen des Layouts START ***
		Label label1 = new Label("Abgegebene Stimmen von Unternehmen");
		Label label2 = new Label("Abgegebene Stimmen von Studenten");
		Label label3 = new Label("Offene Stimmen von Unternehmen");
		Label label4 = new Label("Offene Stimmen von Studenten");

		VerticalLayout v1 = new VerticalLayout(); // Textfelder sollen nebeneinander angeordnet werden
		v1.setWidth("500px");
		v1.add(new HorizontalLayout(comboBox));
		v1.add(new HorizontalLayout(datepickerStartzeitpunktDatum, timepickerStartzeitpunktUhrzeit));
		v1.add(new HorizontalLayout(datepickerEndzeitpunktDatum, timepickerEndzeitpunktUhrzeit));
		v1.add(new HorizontalLayout(textfieldRundendauerInMinuten, checkboxAbgeschlossen));
		v1.add(new HorizontalLayout(buttonMatchingDuerchfuehren, buttonZurueck, logoutButton));
		v1.add(pdfLink);
		v1.add(labelPassword);

		VerticalLayout v2 = new VerticalLayout();
		v2.setHeight("800px");
		v2.setWidth("400px");
		unternehmenGrid.setWidth("400px");
		unternehmenGrid.setHeight("850px");
		studierenderGrid.setWidth("400px");
		studierenderGrid.setHeight("850px");
		v2.add(label1);
		v2.add(unternehmenGrid);
		v2.add(label2);
		v2.add(studierenderGrid);

		VerticalLayout v3 = new VerticalLayout();
		v3.setHeight("800px");
		v3.setWidth("400px");
		unternehmenGrid2.setWidth("400px");
		unternehmenGrid2.setHeight("850px");
		studierenderGrid2.setWidth("400px");
		studierenderGrid2.setHeight("850px");
		v3.add(label3);
		v3.add(unternehmenGrid2);
		v3.add(label4);
		v3.add(studierenderGrid2);

		add(v1);
		add(v2);
		add(v3);
		// *** Erzeugen des Layouts ENDE ***
	}

	private void initialize() {

		studentKontaktWuensche.clear();
		unternehmenKontaktWuensche.clear();
		listOfStudOffeneStimmen.clear();
		listOfStudVotedStimmen.clear();
		listOfUnternOffeneStimmen.clear();
		listOfUnternVotedStimmen.clear();

		ListDataProvider<Studierender> ldpStudent = DataProvider.ofCollection(listOfStudVotedStimmen);
		studierenderGrid.setDataProvider(ldpStudent);
		ListDataProvider<Studierender> ldpStudent2 = DataProvider.ofCollection(listOfStudOffeneStimmen);
		studierenderGrid2.setDataProvider(ldpStudent2);
		ListDataProvider<Unternehmen> ldpUnternehmen = DataProvider.ofCollection(listOfUnternVotedStimmen);
		unternehmenGrid.setDataProvider(ldpUnternehmen);
		ListDataProvider<Unternehmen> ldpUnternehmen2 = DataProvider.ofCollection(listOfUnternOffeneStimmen);
		unternehmenGrid2.setDataProvider(ldpUnternehmen2);

	}

}
