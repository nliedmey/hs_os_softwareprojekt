package de.swprojekt.speeddating.ui;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.alterevent.IAlterEventService;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;

@Route(value = "ui/events/alter", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
public class AlterEvent extends VerticalLayout {
	@Autowired // Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public AlterEvent(IShowEventService iShowEventService, IShowStudierendeService iShowStudierendeService, IShowUnternehmenService iShowUnternehmenService, IAlterEventService iAlterEventService) {

		Grid<Event> eventGrid; // Tabelle mit Events
		GridSingleSelectionModel<Event> selectionModelEvent;
		
		Grid<Studierender> studierenderGrid; // Tabelle mit beteiligten Studierenden
		GridMultiSelectionModel<Studierender> selectionModelStud;
		
		Grid<Unternehmen> unternehmenGrid; // Tabelle mit beteiligten Studierenden
		GridMultiSelectionModel<Unternehmen> selectionModelUnternehmen;
		
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
		
		eventGrid.setSelectionMode(SelectionMode.SINGLE);	//es kann immer nur ein Event gleichzeitig bearbeitet werden
		selectionModelEvent = (GridSingleSelectionModel<Event>) eventGrid.getSelectionModel();

		studierenderGrid = new Grid<>(Studierender.class); // Tabelle initialisieren
		ListDataProvider<Studierender> ldpStudent = DataProvider
				.ofCollection(iShowStudierendeService.showStudierende()); // Dataprovider erstellen und Quelle fuer
																			// Studierende (via Service aus DB)
																			// festlegen
		studierenderGrid.setDataProvider(ldpStudent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		studierenderGrid.removeColumnByKey("student_id");	//studId nicht in Tabelle mit anzeigen
		studierenderGrid.setColumns("vorname", "nachname");	//Spaltenordnung festlegen
		
		studierenderGrid.setSelectionMode(SelectionMode.MULTI);	//es koennen mehrere Studierende ausgewaehlt sein
		selectionModelStud = (GridMultiSelectionModel<Studierender>) studierenderGrid.getSelectionModel();
		
		
		unternehmenGrid = new Grid<>(Unternehmen.class); // Tabelle initialisieren
		ListDataProvider<Unternehmen> ldpUnternehmen = DataProvider
				.ofCollection(iShowUnternehmenService.showUnternehmen()); // Dataprovider erstellen und Quelle fuer
																			// Studierende (via Service aus DB)
																			// festlegen
		unternehmenGrid.setDataProvider(ldpUnternehmen); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		unternehmenGrid.removeColumnByKey("unternehmen_id");	//studId nicht in Tabelle mit anzeigen
		unternehmenGrid.setColumns("unternehmensname", "ansprechpartner","kontaktmail");	//Spaltenordnung festlegen
		
		unternehmenGrid.setSelectionMode(SelectionMode.MULTI);	//es koennen mehrere Unternehmen ausgewaehlt sein
		
		eventGrid.addSelectionListener(event->{
			if(!selectionModelEvent.getFirstSelectedItem().isEmpty())
			{
				Optional<Event> selectedEvent=selectionModelEvent.getFirstSelectedItem();
				Event zuAendernderndesEvent=iShowEventService.showEvent(selectedEvent.get().getEvent_id());
				textfieldBezeichnung.setValue(zuAendernderndesEvent.getBezeichnung());
				//Uhrzeit und Datum aus Date herausfiltern und in date/timepicker einsetzen
				datepickerStartzeitpunktDatum.setValue(zuAendernderndesEvent.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()); 
				timepickerStartzeitpunktUhrzeit.setValue(zuAendernderndesEvent.getStartzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
				datepickerEndzeitpunktDatum.setValue(zuAendernderndesEvent.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				timepickerEndzeitpunktUhrzeit.setValue(zuAendernderndesEvent.getEndzeitpunkt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
				checkboxAbgeschlossen.setValue(zuAendernderndesEvent.isAbgeschlossen());
				Collection<Integer> listStudentenInUnveraendertemEvent=new ArrayList<>(iShowEventService.showEvent(zuAendernderndesEvent.getEvent_id()).getTeilnehmendeStudierende());
				Collection<Integer> listUnternehmenInUnveraendertemEvent=new ArrayList<>(iShowEventService.showEvent(zuAendernderndesEvent.getEvent_id()).getTeilnehmendeUnternehmen());
			
				studierenderGrid.deselectAll(); //zunaechst alle ausgewaehlten von vorheriger Eventmarkierung entfernen
				for(Studierender s:ldpStudent.getItems())
				{
					if(listStudentenInUnveraendertemEvent.contains(s.getStudent_id())) //wenn Studierender in Event inkludiert
					{ 
						studierenderGrid.select(s); //Studierende im Event in Tabelle markieren
					}	
				}
				
				unternehmenGrid.deselectAll(); //zunaechst alle ausgewaehlten von vorheriger Eventmarkierung entfernen
				for(Unternehmen u:ldpUnternehmen.getItems())
				{
					if(listUnternehmenInUnveraendertemEvent.contains(u.getUnternehmen_id())) //wenn Unternehmen in Event inkludiert
					{
						unternehmenGrid.select(u);
					}	
				}
			}
		});
		
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
			Set<Integer> studentenInUnveraendertemEvent=new HashSet<>(veraendertesEventDAO.getTeilnehmendeStudierende());
			Set<Integer> studentenInVeraendertemEvent=new HashSet<>();
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
		v1.add(unternehmenGrid);
		v1.add(aendernButton);
		add(v1);
		
	}
}
