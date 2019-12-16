package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.addevent.IAddEventService;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;

/*
 * View zum Hinzufuegen von Event
 */
@Route(value = "ui/events/add", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
@Secured("ROLE_EVENTORGANISATOR")
public class AddEvent extends VerticalLayout {

	@Autowired // BestPractice: Konstruktor-Injection im Vergleich zu
				// Attribut/Methoden-Injection
				// Parameter (hier: IAddStudierenderService) wird also automatisch autowired
	public AddEvent(IAddEventService iAddEventService, IShowStudierendeService iShowStudierendeService, IShowUnternehmenService iShowUnternehmenService) {

		// Deklaration
		Binder<Event> binder; // verknuepft Input aus Textfeldern mit Objektattributen
		Grid<Studierender> studierenderGrid;	//Tabelle mit Studierenden, welche Event zugeordnet werden sollen
		GridMultiSelectionModel<Studierender> selectionModelStud; //es sollen mehrere Studierende aus Tabelle ausgewaehlt werden koennen
		Grid<Unternehmen> unternehmenGrid;	//Tabelle mit Unternehmen, welche Event zugeordnet werden sollen
		GridMultiSelectionModel<Unternehmen> selectionModelUnternehmen; //es sollen mehrere Studierende aus Tabelle ausgewaehlt werden koennen

		// Erzeugen der Input Felder
		TextField textfieldBezeichnung = new TextField("Bezeichnung:");
		DatePicker datepickerStartzeitpunktDatum=new DatePicker("Startdatum:");
		TimePicker timepickerStartzeitpunktUhrzeit=new TimePicker("Startzeit:");
		TextField textfieldRundendauerInMinuten = new TextField("Rundendauer (min):");
		DatePicker datepickerEndzeitpunktDatum=new DatePicker("Enddatum:");
		TimePicker timepickerEndzeitpunktUhrzeit=new TimePicker("Endzeit:");
		
		Button logoutButton=new Button("Logout");
		
		studierenderGrid = new Grid<>(Studierender.class);	//Tabelle initialisieren
		ListDataProvider<Studierender> ldpStudent = DataProvider
				.ofCollection(iShowStudierendeService.showStudierende());	//Dataprovider erstellen und Quelle fuer Studierende (via Service aus DB) festlegen 
		studierenderGrid.setDataProvider(ldpStudent);	//erstellten Dataprovider als Datenquelle fuer Tabelle festlegen
		studierenderGrid.removeColumnByKey("student_id");	//studId nicht in Tabelle mit anzeigen
		studierenderGrid.setColumns("vorname", "nachname");	//Spaltenordnung festlegen
		studierenderGrid.setSelectionMode(SelectionMode.MULTI); //multiselection bedeutet Auswahl mehrerer Studierender in Tabelle moeglich
		selectionModelStud = (GridMultiSelectionModel<Studierender>) studierenderGrid.getSelectionModel();
		
		unternehmenGrid = new Grid<>(Unternehmen.class);	//Tabelle initialisieren
		ListDataProvider<Unternehmen> ldpUnternehmen = DataProvider
				.ofCollection(iShowUnternehmenService.showUnternehmen());	//Dataprovider erstellen und Quelle fuer Unternehmen (via Service aus DB) festlegen 
		unternehmenGrid.setDataProvider(ldpUnternehmen);	//erstellten Dataprovider als Datenquelle fuer Tabelle festlegen
		unternehmenGrid.removeColumnByKey("unternehmen_id");	//studId nicht in Tabelle mit anzeigen
		unternehmenGrid.setColumns("unternehmensname", "ansprechpartner","kontaktmail");	//Spaltenordnung festlegen
		unternehmenGrid.setSelectionMode(SelectionMode.MULTI); //multiselection bedeutet Auswahl mehrerer Unternehmen in Tabelle moeglich
		selectionModelUnternehmen = (GridMultiSelectionModel<Unternehmen>) unternehmenGrid.getSelectionModel();
		
		// Button hinzufuegen
		Button buttonHinzufuegen = new Button("Event anlegen");
		buttonHinzufuegen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		Button zurueckButton = new Button("Zurueck");

		// Notification Meldungen mit Button verknuepfen
		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Event erfolgreich hinzugefuegt! ");
		notificationSavesuccess.add(labelSavesuccess);
		notificationSavesuccess.setDuration(2500); //Meldung wird 2,5 Sekunden lang angezeigt
				
		VerticalLayout v1 = new VerticalLayout(); // Textfelder sollen untereinander angeordnet werden
		v1.add(textfieldBezeichnung);
		v1.add(new HorizontalLayout(datepickerStartzeitpunktDatum,timepickerStartzeitpunktUhrzeit));
		v1.add(new HorizontalLayout(datepickerEndzeitpunktDatum,timepickerEndzeitpunktUhrzeit));
		v1.add(textfieldRundendauerInMinuten);
		HorizontalLayout h1=new HorizontalLayout();
		h1.add(unternehmenGrid,studierenderGrid);
		v1.add(unternehmenGrid);
		v1.add(studierenderGrid);
//		v1.add(h1);
				
		v1.add(buttonHinzufuegen); // darunter wird Button angeordnet
		v1.add(new HorizontalLayout(zurueckButton, logoutButton));
		add(v1);
		
		binder = new Binder<>(Event.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)

		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		binder.forField(textfieldBezeichnung).asRequired("Bezeichnung darf nicht leer sein...").bind("bezeichnung");
		binder.forField(textfieldRundendauerInMinuten).withConverter(new StringToIntegerConverter("Dauer muss numerisch sein")).asRequired("Dauer darf nicht leer sein...").bind("rundendauerInMinuten");
		//binder.forField(textfieldHausnummer).withConverter(new StringToIntegerConverter("Eingabe muss numerisch sein")).bind("hausnummer");
		
		Event einEvent = new Event();
		buttonHinzufuegen.addClickListener(event -> {
			try {
				binder.writeBean(einEvent); // dem Objekt werden Attributwerte aus den Textfeldern (via Binder) zugewiesen
				
				//Verarbeitung der Datums- und Uhrzeiteingabe ueber Date- und Timepicker
				LocalDate startzeitpunktDatum=datepickerStartzeitpunktDatum.getValue(); //Datumswert aus Datepicker ziehen
				LocalTime startzeitpunktUhrzeit=timepickerStartzeitpunktUhrzeit.getValue();	//Zeitwert aus Timepicker ziehen
				
				LocalDate endzeitpunktDatum=datepickerEndzeitpunktDatum.getValue();
				LocalTime endzeitpunktUhrzeit=timepickerEndzeitpunktUhrzeit.getValue();
				
				LocalDateTime startzeitpunktldt=LocalDateTime.of(startzeitpunktDatum, startzeitpunktUhrzeit); //zusammenfuegen von Date und Time zu DateTime
				Date startzeitpunkt=Date.from(startzeitpunktldt.atZone(ZoneId.systemDefault()).toInstant()); //Umwandlung DateTime in Date, weil Objektattribut in Studierender-Klasse von diesem Typ
				
				LocalDateTime endzeitpunktldt=LocalDateTime.of(endzeitpunktDatum, endzeitpunktUhrzeit);
				Date endzeitpunkt=Date.from(endzeitpunktldt.atZone(ZoneId.systemDefault()).toInstant());
				
				einEvent.setStartzeitpunkt(startzeitpunkt);
				einEvent.setEndzeitpunkt(endzeitpunkt);
				
				einEvent.setAbgeschlossen(false); //beim erstellen ist das Event nicht abgeschlossen
				
				Set<Integer> teilnehmendeStudierende=new HashSet<>();
				for(Studierender einStudierender:selectionModelStud.getSelectedItems()) {	//IDs der teilnehmenden Studierenden aus Tabelle einsammeln
					teilnehmendeStudierende.add(einStudierender.getStudent_id());	
				}
				einEvent.setTeilnehmendeStudierende(teilnehmendeStudierende);
				
				Set<Integer> teilnehmendeUnternehmen=new HashSet<>();
				for(Unternehmen einUnternehmen:selectionModelUnternehmen.getSelectedItems()) {	//IDs der teilnehmenden Studierenden aus Tabelle einsammeln
					teilnehmendeUnternehmen.add(einUnternehmen.getUnternehmen_id());	
				}
				einEvent.setTeilnehmendeUnternehmen(teilnehmendeUnternehmen);
				Event erstelltesEvent=iAddEventService.speicherEvent(einEvent); // Uebergabe an Service zur Speicherung in DB
				
				CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
				iAddEventService.addeEventInEventorga(erstelltesEvent.getEvent_id(), userDetails.getEntityRefId() ); //dem Eventorganisator dieses Event zuordnen (er verwaltet dies)
				notificationSavesuccess.open(); // Erfolgreich-Meldung anzeigen
				buttonHinzufuegen.getUI().ifPresent(ui->ui.navigate("ui/eventorganisator/menue"));	//zurueck auf andere Seite 
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("login"));	//zurueck auf andere Seite 
		});
		
		zurueckButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			zurueckButton.getUI().ifPresent(ui->ui.navigate("ui/eventorganisator/menue"));	//zurueck auf andere Seite 
		});


	}
}
