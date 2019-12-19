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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.alterevent.IAlterEventService;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;

@Route(value = "ui/events/alterForOrganisator", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
@Secured("ROLE_EVENTORGANISATOR")
public class AlterEventForEventorganisator extends VerticalLayout {
	@Autowired // Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public AlterEventForEventorganisator(IShowEventService iShowEventService, IShowStudierendeService iShowStudierendeService, IShowUnternehmenService iShowUnternehmenService, IAlterEventService iAlterEventService) {

		Binder<Event> binder; // verknuepft Input aus Textfeldern mit Objektattributen
		
		Grid<Event> eventGrid; // Tabelle mit Events
		GridSingleSelectionModel<Event> selectionModelEvent;
		
		Grid<Studierender> studierenderGrid; // Tabelle mit beteiligten Studierenden
		GridMultiSelectionModel<Studierender> selectionModelStud;
		
		Grid<Unternehmen> unternehmenGrid; // Tabelle mit beteiligten Studierenden
		GridMultiSelectionModel<Unternehmen> selectionModelUnternehmen;
		
		Button aendernButton=new Button("Aendern");
		Button logoutButton=new Button("Logout");
		Button zurueckButton = new Button("Zurueck");
		
		TextField textfieldBezeichnung = new TextField("Bezeichnung:");
		DatePicker datepickerStartzeitpunktDatum=new DatePicker("Startdatum:");
		TimePicker timepickerStartzeitpunktUhrzeit=new TimePicker("Startzeit:");
		DatePicker datepickerEndzeitpunktDatum=new DatePicker("Enddatum:");
		TimePicker timepickerEndzeitpunktUhrzeit=new TimePicker("Endzeit:");
		TextField textfieldRundendauerInMinuten = new TextField("Rundendauer (min):");
		Checkbox checkboxAbgeschlossen=new Checkbox("Abgeschlossen:");
		
		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Event erfolgreich aktualisiert! ");
		notificationSavesuccess.add(labelSavesuccess);
		notificationSavesuccess.setDuration(2500); //Meldung wird 2,5 Sekunden lang angezeigt
		
		List<Event> listOfEvents = new ArrayList<Event>();
		CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	//Id des eingeloggten Users aus SecurityKontext holen
		
		//Hier auskommentieren, wenn Events nur fuer den Organisator geladen werden sollen
		for(int event_id:iShowEventService.showEventsOfUser(userDetails.getEntityRefId()))	//alle Events, welche von Eventorganisator verwaltet, laden
		{
			listOfEvents.add(iShowEventService.showEvent(event_id));
		}
//		listOfEvents.addAll(iShowEventService.showEvents());
		
		
		eventGrid = new Grid<>(Event.class); // Tabelle initialisieren
		ListDataProvider<Event> ldpEvent = DataProvider
				.ofCollection(listOfEvents); // Dataprovider erstellen und Quelle fuer
																			// Events (via Service aus DB)
																			// festlegen
		eventGrid.setDataProvider(ldpEvent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		eventGrid.removeColumnByKey("event_id");	//event_id nicht in Tabelle mit anzeigen
		eventGrid.setColumns("bezeichnung", "startzeitpunkt", "endzeitpunkt", "abgeschlossen", "teilnehmendeStudierende","teilnehmendeUnternehmen");	//Spaltenordnung festlegen
		
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
		selectionModelUnternehmen = (GridMultiSelectionModel<Unternehmen>) unternehmenGrid.getSelectionModel();
		
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
				textfieldRundendauerInMinuten.setValue(String.valueOf(zuAendernderndesEvent.getRundendauerInMinuten()));	
				checkboxAbgeschlossen.setValue(zuAendernderndesEvent.isAbgeschlossen());
				Collection<Integer> listStudentenInUnveraendertemEvent=new ArrayList<>(iShowEventService.showEvent(zuAendernderndesEvent.getEvent_id()).getTeilnehmendeStudierende());
				Collection<Integer> listUnternehmenInUnveraendertemEvent=new ArrayList<>(iShowEventService.showEvent(zuAendernderndesEvent.getEvent_id()).getTeilnehmendeUnternehmen());
			
				
				if (zuAendernderndesEvent.isAbgeschlossen() == true) {

					aendernButton.setEnabled(false);					
					textfieldBezeichnung.setEnabled(false);
					datepickerStartzeitpunktDatum.setEnabled(false);
					timepickerStartzeitpunktUhrzeit.setEnabled(false);
					datepickerEndzeitpunktDatum.setEnabled(false);
					timepickerEndzeitpunktUhrzeit.setEnabled(false);
					textfieldRundendauerInMinuten.setEnabled(false);
					checkboxAbgeschlossen.setEnabled(false);
					unternehmenGrid.setEnabled(false);
					studierenderGrid.setEnabled(false);
					
				} else {
					aendernButton.setEnabled(true);					
					textfieldBezeichnung.setEnabled(true);
					datepickerStartzeitpunktDatum.setEnabled(true);
					timepickerStartzeitpunktUhrzeit.setEnabled(true);
					datepickerEndzeitpunktDatum.setEnabled(true);
					timepickerEndzeitpunktUhrzeit.setEnabled(true);
					textfieldRundendauerInMinuten.setEnabled(true);
					checkboxAbgeschlossen.setEnabled(true);
					unternehmenGrid.setEnabled(true);
					studierenderGrid.setEnabled(true);
					
				}
				
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
		
		binder = new Binder<>(Event.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)

		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		binder.forField(textfieldBezeichnung).asRequired("Bezeichnung darf nicht leer sein...").bind("bezeichnung");
		binder.forField(textfieldRundendauerInMinuten).withConverter(new StringToIntegerConverter("Dauer muss numerisch sein")).asRequired("Dauer darf nicht leer sein...").bind("rundendauerInMinuten");
		//binder.forField(textfieldHausnummer).withConverter(new StringToIntegerConverter("Eingabe muss numerisch sein")).bind("hausnummer");
		
		aendernButton.addClickListener(event->{
			Optional<Event> selectedEvent=selectionModelEvent.getFirstSelectedItem();
			Event veraendertesEventDAO=iShowEventService.showEvent(selectedEvent.get().getEvent_id());
			if(!textfieldBezeichnung.getValue().equals(veraendertesEventDAO.getBezeichnung()))
			{
				veraendertesEventDAO.setBezeichnung(textfieldBezeichnung.getValue());
			}
			if(!textfieldRundendauerInMinuten.getValue().equals(String.valueOf(veraendertesEventDAO.getRundendauerInMinuten())))
			{
				veraendertesEventDAO.setRundendauerInMinuten(Integer.valueOf(textfieldRundendauerInMinuten.getValue()));
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
			
			Set<Integer> unternehmenInUnveraendertemEvent=new HashSet<>(veraendertesEventDAO.getTeilnehmendeUnternehmen());
			Set<Integer> unternehmenInVeraendertemEvent=new HashSet<>();
			
			for(Studierender einAusgewaehlterStudierender:selectionModelStud.getSelectedItems())
			{
				studentenInVeraendertemEvent.add(einAusgewaehlterStudierender.getStudent_id());
			}
			for(Unternehmen einAusgewaehltesUnternehmen:selectionModelUnternehmen.getSelectedItems())
			{
				unternehmenInVeraendertemEvent.add(einAusgewaehltesUnternehmen.getUnternehmen_id());
			}
			if(!studentenInUnveraendertemEvent.equals(studentenInVeraendertemEvent))
			{
				System.out.println("Teilnehmende Studenten veraendert!");
				veraendertesEventDAO.setTeilnehmendeStudierende(studentenInVeraendertemEvent);
			}
			if(!unternehmenInUnveraendertemEvent.equals(unternehmenInVeraendertemEvent))
			{
				System.out.println("Teilnehmende Unternehmen veraendert!");
				veraendertesEventDAO.setTeilnehmendeUnternehmen(unternehmenInVeraendertemEvent);
			}
			iAlterEventService.aenderEvent(veraendertesEventDAO);
			notificationSavesuccess.open();
			aendernButton.getUI().ifPresent(ui->ui.navigate("ui/eventorganisator/menue"));	//zurueck auf andere Seite 
		});
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			//getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("login"));	//zurueck auf andere Seite 
		});
		
		zurueckButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			zurueckButton.getUI().ifPresent(ui->ui.navigate("ui/eventorganisator/menue"));	//zurueck auf andere Seite 
		});

		
		VerticalLayout v1 = new VerticalLayout(); // Textfelder sollen untereinander angeordnet werden
		v1.add(eventGrid);
		v1.add(textfieldBezeichnung);
		v1.add(new HorizontalLayout(datepickerStartzeitpunktDatum,timepickerStartzeitpunktUhrzeit));
		v1.add(new HorizontalLayout(datepickerEndzeitpunktDatum,timepickerEndzeitpunktUhrzeit));
		v1.add(new HorizontalLayout(textfieldRundendauerInMinuten, checkboxAbgeschlossen));
		v1.add(studierenderGrid);
		v1.add(unternehmenGrid);
		v1.add(aendernButton);
		v1.add(new HorizontalLayout(zurueckButton, logoutButton));
		add(v1);
		
	}
}
