package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;
import de.swprojekt.speeddating.service.unternehmen.IUnternehmenService;

/*
 * 
 */
@Route(value = "ui/untern/EventMatchingDisplayView", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
public class EventMatchingDisplayView extends HorizontalLayout {

	int lv_id = 0;
	// BestPractice: Konstruktor-Injection im Vergleich zu
	// Attribut/Methoden-Injection
	// Parameter (hier: IAddStudierenderService) wird also automatisch autowired

	
	
	@Autowired
	public EventMatchingDisplayView(IShowEventService iShowEventService, IShowStudierendeService iShowStudierendeService) {
		
		Binder<Event> binder; // verknuepft Input aus Textfeldern mit Objektattributen		
		TextField textfieldBezeichnung = new TextField("Bezeichnung:");
		DatePicker datepickerStartzeitpunktDatum=new DatePicker("Startdatum:");
		TimePicker timepickerStartzeitpunktUhrzeit=new TimePicker("Startzeit:");
		DatePicker datepickerEndzeitpunktDatum=new DatePicker("Enddatum:");
		TimePicker timepickerEndzeitpunktUhrzeit=new TimePicker("Endzeit:");
		TextField textfieldRundendauerInMinuten = new TextField("Rundendauer (min):");
		Checkbox checkboxAbgeschlossen=new Checkbox("Abgeschlossen:");
		
		GridMultiSelectionModel<Studierender> selectionModelStud;
		Grid<Studierender> studierenderGrid; // Tabelle mit beteiligten Studierenden
		studierenderGrid = new Grid<>(Studierender.class); // Tabelle initialisieren
			
		
		// Erzeugen der Combo Box
		ComboBox<Event> comboBox = new ComboBox<>();
		comboBox.setLabel("Event auswaehlen");
		comboBox.setItemLabelGenerator(Event::getBezeichnung);
		List<Event> listOfEvents = iShowEventService.showEvents();
		comboBox.setItems(listOfEvents);
		comboBox.addValueChangeListener(event -> {
			Event aEvent = comboBox.getValue();
			if (aEvent != null) {
				lv_id = aEvent.getEvent_id();
				Event selectedEvent = iShowEventService.showEvent(lv_id);				
				textfieldBezeichnung.setValue(selectedEvent.getBezeichnung());
				//Uhrzeit und Datum aus Date herausfiltern und in date/timepicker einsetzen
				datepickerStartzeitpunktDatum.setValue(selectedEvent.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()); 
				timepickerStartzeitpunktUhrzeit.setValue(selectedEvent.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
				datepickerEndzeitpunktDatum.setValue(selectedEvent.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				timepickerEndzeitpunktUhrzeit.setValue(selectedEvent.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
				textfieldRundendauerInMinuten.setValue(String.valueOf(selectedEvent.getRundendauerInMinuten()));
				checkboxAbgeschlossen.setValue(selectedEvent.isAbgeschlossen());
				
				List<Studierender> listOfStudierenden =  new ArrayList<Studierender>();				
				for (Integer student_id : selectedEvent.getTeilnehmendeStudierende()) {					
					Studierender aStudent = iShowStudierendeService.showStudierenden(student_id);
					listOfStudierenden.add(aStudent);
				}				
				
				ListDataProvider<Studierender> ldpStudent = DataProvider
						.ofCollection(listOfStudierenden); 
				studierenderGrid.setDataProvider(ldpStudent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen
				
							

			} else {
				// message.setText("No song is selected");
			}
		});
				
		studierenderGrid.setSelectionMode(SelectionMode.MULTI);	//es koennen mehrere Studierende ausgewaehlt sein
		selectionModelStud = (GridMultiSelectionModel<Studierender>) studierenderGrid.getSelectionModel();
		studierenderGrid.removeColumnByKey("student_id");	//studId nicht in Tabelle mit anzeigen
		studierenderGrid.setColumns("vorname", "nachname");	//Spaltenordnung festlegen

		// Button #1 hinzufuegen
		Button buttonMatchingDuerchfuehren = new Button("Matching durchfuehren");
		buttonMatchingDuerchfuehren.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		// Button #2 hinzufuegen
		Button buttonZurueck = new Button("Zurueck");
		buttonZurueck.addThemeVariants(ButtonVariant.LUMO_ERROR);

		// Notification Meldungen mit Button verknuepfen
		Notification notificationMatchingsuccess = new Notification();
		notificationMatchingsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelMatchingsuccess = new Label("Unternehmen erfolgreich aktualisiert! ");
		notificationMatchingsuccess.add(labelMatchingsuccess);

		Notification notificationZurueck = new Notification();
		notificationZurueck.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelZuruecksuccess = new Label("Bearbeitung abgebrochen! ");
		notificationZurueck.add(labelZuruecksuccess);

		buttonZurueck.addClickListener(event -> {
			// Erfolgreich-Meldung anzeigen
			notificationZurueck.open();
//			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//			getUI().get().getSession().close();		//Vaadin Session leeren
			buttonZurueck.getUI().ifPresent(ui -> ui.navigate("maincontent")); // zurueck auf andere Seite
		});

		
		
		// *** Erzeugen des Layouts START ***
		
		studierenderGrid.setHeight("100px");
		VerticalLayout v1 = new VerticalLayout(); // Textfelder sollen nebeneinander angeordnet werden
		v1.add(comboBox);
		v1.add(textfieldBezeichnung);
		v1.add(new HorizontalLayout(datepickerStartzeitpunktDatum,timepickerStartzeitpunktUhrzeit));
		v1.add(new HorizontalLayout(datepickerEndzeitpunktDatum,timepickerEndzeitpunktUhrzeit));
		v1.add(textfieldRundendauerInMinuten);
		v1.add(buttonMatchingDuerchfuehren, buttonZurueck);
		
		VerticalLayout v2 = new VerticalLayout(); // Textfelder sollen nebeneinander angeordnet werden
		v2.add(studierenderGrid);
		
		add(v1);
		add(v2);
		// *** Erzeugen des Layouts ENDE ***
		
		
	}

}
