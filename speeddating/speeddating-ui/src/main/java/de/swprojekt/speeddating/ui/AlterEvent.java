package de.swprojekt.speeddating.ui;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.service.alterevent.IAlterEventService;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;

@Route(value = "ui/events/alter", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
public class AlterEvent extends VerticalLayout {
	@Autowired // Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public AlterEvent(IShowEventService iShowEventService, IShowStudierendeService iShowStudierendeService, IAlterEventService iAlterEventService) {

		Grid<Event> eventGrid; // Tabelle mit Events
		GridMultiSelectionModel<Event> selectionModelEvent;
		
		Grid<Studierender> studierenderGrid; // Tabelle mit beteiligten Studierenden
		GridMultiSelectionModel<Studierender> selectionModelStud;
		
		Button aendernButton=new Button("Aendern");
		
		TextField textfieldBezeichnung = new TextField("Bezeichnung:");
		DatePicker datepickerStartzeitpunktDatum=new DatePicker("Startdatum:");
		TimePicker timepickerStartzeitpunktUhrzeit=new TimePicker("Startzeit:");
		DatePicker datepickerEndzeitpunktDatum=new DatePicker("Enddatum:");
		TimePicker timepickerEndzeitpunktUhrzeit=new TimePicker("Endzeit:");
		Checkbox checkboxAbgeschlossen=new Checkbox("Abgeschlossen:");
		

		eventGrid = new Grid<>(Event.class); // Tabelle initialisieren
		ListDataProvider<Event> ldpEvent = DataProvider
				.ofCollection(iShowEventService.showEvents()); // Dataprovider erstellen und Quelle fuer
																			// Events (via Service aus DB)
																			// festlegen
		eventGrid.setDataProvider(ldpEvent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		eventGrid.removeColumnByKey("event_id");	//event_id nicht in Tabelle mit anzeigen
		eventGrid.setColumns("bezeichnung", "startzeitpunkt", "endzeitpunkt", "abgeschlossen", "teilnehmendeStudierende");	//Spaltenordnung festlegen
		
		eventGrid.setSelectionMode(SelectionMode.MULTI);
		selectionModelEvent = (GridMultiSelectionModel<Event>) eventGrid.getSelectionModel();

		eventGrid.addSelectionListener(event->{
			Optional<Event> selectedEvent=selectionModelEvent.getFirstSelectedItem();
			Event zuAendernderndesEvent=iShowEventService.showEvent(selectedEvent.get().getEvent_id());
			textfieldBezeichnung.setValue(zuAendernderndesEvent.getBezeichnung());
			//Uhrzeit und Datum aus Date herausfiltern und in date/timepicker einsetzen
			datepickerStartzeitpunktDatum.setValue(zuAendernderndesEvent.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()); 
			timepickerStartzeitpunktUhrzeit.setValue(zuAendernderndesEvent.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
			datepickerEndzeitpunktDatum.setValue(zuAendernderndesEvent.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			timepickerEndzeitpunktUhrzeit.setValue(zuAendernderndesEvent.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
			checkboxAbgeschlossen.setValue(zuAendernderndesEvent.isAbgeschlossen());
			
		});
		
		studierenderGrid = new Grid<>(Studierender.class); // Tabelle initialisieren
		ListDataProvider<Studierender> ldpStudent = DataProvider
				.ofCollection(iShowStudierendeService.showStudierende()); // Dataprovider erstellen und Quelle fuer
																			// Studierende (via Service aus DB)
																			// festlegen
		studierenderGrid.setDataProvider(ldpStudent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		studierenderGrid.removeColumnByKey("studId"); // studId nicht in Tabelle mit anzeigen
		studierenderGrid.setColumns("vorname", "nachname", "hauptfach"); // Spaltenordnung festlegen
		
		studierenderGrid.setSelectionMode(SelectionMode.MULTI);
		selectionModelStud = (GridMultiSelectionModel<Studierender>) studierenderGrid.getSelectionModel();
		
		aendernButton.addClickListener(event->{
			Optional<Event> selectedEvent=selectionModelEvent.getFirstSelectedItem();
			Event veraendertesEventDAO=iShowEventService.showEvent(selectedEvent.get().getEvent_id());
			if(!textfieldBezeichnung.getValue().equals(veraendertesEventDAO.getBezeichnung()))
			{
				veraendertesEventDAO.setBezeichnung(textfieldBezeichnung.getValue());
			}
			//wenn Datepicker oder Timepicker fuer Anfangszeitpunkt veraendert
			if((!datepickerStartzeitpunktDatum.getValue().equals(veraendertesEventDAO.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))||(!timepickerStartzeitpunktUhrzeit.getValue().equals(veraendertesEventDAO.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())))
			{
				veraendertesEventDAO.setStartzeitpunkt(Date.from(LocalDateTime.of(datepickerStartzeitpunktDatum.getValue(), timepickerStartzeitpunktUhrzeit.getValue()).atZone(ZoneId.systemDefault()).toInstant()));
			}
			//wenn Datepicker oder Timepicker fuer Endzeitpunkt veraendert
			if((!datepickerEndzeitpunktDatum.getValue().equals(veraendertesEventDAO.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))||(!timepickerEndzeitpunktUhrzeit.getValue().equals(veraendertesEventDAO.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())))
			{
				veraendertesEventDAO.setEndzeitpunkt(Date.from(LocalDateTime.of(datepickerEndzeitpunktDatum.getValue(), timepickerEndzeitpunktUhrzeit.getValue()).atZone(ZoneId.systemDefault()).toInstant()));
			}
			if(!checkboxAbgeschlossen.getValue()==true)
			{
				veraendertesEventDAO.setAbgeschlossen(checkboxAbgeschlossen.getValue());
			}
			//Testen, ob gleiche Studierende und Unternehmen zu Event zugeordnet
			ArrayList<Integer> studentenInUnveraendertemEvent=new ArrayList<>(veraendertesEventDAO.getTeilnehmendeStudierende());
			ArrayList<Integer> studentenInVeraendertemEvent=new ArrayList<>();
			for(Studierender einAusgewaehlterStudierender:selectionModelStud.getSelectedItems())
			{
				studentenInVeraendertemEvent.add(einAusgewaehlterStudierender.getStudent_id());
			}
			if(!studentenInUnveraendertemEvent.equals(studentenInVeraendertemEvent))
			{
				System.out.println("Teilnehmende Studenten veraendert!");
				veraendertesEventDAO.setTeilnehmendeStudierende(studentenInVeraendertemEvent);
			}
			iAlterEventService.aenderEvent(veraendertesEventDAO);
		});
		
		VerticalLayout v1 = new VerticalLayout(); // Textfelder sollen untereinander angeordnet werden
		v1.add(eventGrid);
		v1.add(textfieldBezeichnung);
		v1.add(new HorizontalLayout(datepickerStartzeitpunktDatum,timepickerStartzeitpunktUhrzeit));
		v1.add(new HorizontalLayout(datepickerEndzeitpunktDatum,timepickerEndzeitpunktUhrzeit));
		v1.add(studierenderGrid);
		v1.add(aendernButton);
		
	}
}
