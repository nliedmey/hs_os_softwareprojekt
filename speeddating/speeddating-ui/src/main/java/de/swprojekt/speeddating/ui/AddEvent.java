package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.service.addevent.IAddEventService;

/*
 * View zum Hinzufuegen von Event
 */
@Route(value = "ui/events/add", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
public class AddEvent extends VerticalLayout {

	@Autowired // BestPractice: Konstruktor-Injection im Vergleich zu
				// Attribut/Methoden-Injection
				// Parameter (hier: IAddStudierenderService) wird also automatisch autowired
	public AddEvent(IAddEventService iAddEventService) {

		// Deklaration
		Binder<Event> binder; // verknuepft Input aus Textfeldern mit Objektattributen

		// Erzeugen der Input Felder
		TextField textfieldBezeichnung = new TextField("Bezeichnung:");
		DatePicker datepickerStartzeitpunktDatum=new DatePicker("Startdatum:");
		TimePicker timepickerStartzeitpunktUhrzeit=new TimePicker("Startzeit:");
		DatePicker datepickerEndzeitpunktDatum=new DatePicker("Enddatum:");
		TimePicker timepickerEndzeitpunktUhrzeit=new TimePicker("Endzeit:");

		// Button hinzufuegen
		Button buttonHinzufuegen = new Button("Event anlegen");
		buttonHinzufuegen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		// Notification Meldungen mit Button verknuepfen
		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Event erfolgreich hinzugefuegt! ");
		notificationSavesuccess.add(labelSavesuccess);
				
		VerticalLayout v1 = new VerticalLayout(); // Textfelder sollen untereinander angeordnet werden
		v1.add(textfieldBezeichnung);
		v1.add(new HorizontalLayout(datepickerStartzeitpunktDatum,timepickerStartzeitpunktUhrzeit));
		v1.add(new HorizontalLayout(datepickerEndzeitpunktDatum,timepickerEndzeitpunktUhrzeit));
				
		add(v1, buttonHinzufuegen); // darunter wird Button angeordnet
		
		

		binder = new Binder<>(Event.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)

		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		binder.forField(textfieldBezeichnung).asRequired("Bezeichnung darf nicht leer sein...").bind("bezeichnung");
		//binder.forField(textfieldHausnummer).withConverter(new StringToIntegerConverter("Eingabe muss numerisch sein")).bind("hausnummer");
		
		
		Event einEvent = new Event();
		buttonHinzufuegen.addClickListener(event -> {
			try {
				binder.writeBean(einEvent); // dem Objekt werden Attributwerte aus den Textfeldern (via Binder)
													// zugewiesen
				
				//Verarbeitung der Datums- und Uhrzeiteingabe ueber Date- und Timepicker
				LocalDate startzeitpunktDatum=datepickerStartzeitpunktDatum.getValue();
				LocalTime startzeitpunktUhrzeit=timepickerStartzeitpunktUhrzeit.getValue();
				
				LocalDate endzeitpunktDatum=datepickerEndzeitpunktDatum.getValue();
				LocalTime endzeitpunktUhrzeit=timepickerEndzeitpunktUhrzeit.getValue();
				
				LocalDateTime startzeitpunktldt=LocalDateTime.of(startzeitpunktDatum, startzeitpunktUhrzeit);
				Date startzeitpunkt=Date.from(startzeitpunktldt.atZone(ZoneId.systemDefault()).toInstant());
				
				LocalDateTime endzeitpunktldt=LocalDateTime.of(endzeitpunktDatum, endzeitpunktUhrzeit);
				Date endzeitpunkt=Date.from(endzeitpunktldt.atZone(ZoneId.systemDefault()).toInstant());
				
				einEvent.setStartzeitpunkt(startzeitpunkt);
				einEvent.setEndzeitpunkt(endzeitpunkt);
				
				einEvent.setAbgeschlossen(false); //beim erstellen ist das Event nicht abgeschlossen
				
				iAddEventService.speicherEvent(einEvent); // Uebergabe an Service zur Speicherung
																				// in DB
				notificationSavesuccess.open(); // Erfolgreich-Meldung anzeigen
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});

	}
}
