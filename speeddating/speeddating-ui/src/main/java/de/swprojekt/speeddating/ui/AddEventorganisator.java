package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Eventorganisator;
import de.swprojekt.speeddating.service.addeventorganisator.IAddEventorganisatorService;

/*
 * View zum Hinzufuegen von Eventorganisator
 */
@Route(value = "ui/eventorganisator/add", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
public class AddEventorganisator extends VerticalLayout {

	@Autowired // BestPractice: Konstruktor-Injection im Vergleich zu
				// Attribut/Methoden-Injection
				// Parameter (hier: IAddEventorganisatorService) wird also automatisch autowired
	public AddEventorganisator(IAddEventorganisatorService iAddEventorganisatorService) {

		// Deklaration
		Binder<Eventorganisator> binder; // verknuepft Input aus Textfeldern mit Objektattributen

		// Erzeugen der Input Felder
		TextField textfieldVorname = new TextField("Vorname:");
		TextField textfieldNachname = new TextField("Nachname:");
		TextField textfieldFachbereich = new TextField("Fachbereich:");
		TextField textfieldTelefonnr = new TextField("Telefonnr:");
		TextField textfieldEmail = new TextField("Email:");
		
		// Button hinzufuegen
		Button buttonHinzufuegen = new Button("Eventorganisator anlegen");
		buttonHinzufuegen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		// Notification Meldungen mit Button verknuepfen
		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Eventorganisator erfolgreich hinzugefuegt! ");
		notificationSavesuccess.add(labelSavesuccess);
				
		VerticalLayout v1 = new VerticalLayout(); // Textfelder sollen untereinander angeordnet werden
		v1.add(textfieldVorname);
		v1.add(textfieldNachname);
		v1.add(textfieldFachbereich);
		v1.add(textfieldTelefonnr);
		v1.add(textfieldEmail);
				
		add(v1, buttonHinzufuegen); // darunter wird Button angeordnet
		
		binder = new Binder<>(Eventorganisator.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)

		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		binder.forField(textfieldVorname).asRequired("Vorname darf nicht leer sein...").bind("vorname");
		binder.forField(textfieldNachname).asRequired("Nachname darf nicht leer sein...").bind("nachname");
		binder.forField(textfieldFachbereich).asRequired("Fachbereich darf nicht leer sein...").bind("fachbereich");
		binder.forField(textfieldTelefonnr).asRequired("Telefonnr darf nicht leer sein...").bind("telefonnr");
		binder.forField(textfieldEmail).asRequired("Email darf nicht leer sein...").bind("email");
		//binder.forField(textfieldHausnummer).withConverter(new StringToIntegerConverter("Eingabe muss numerisch sein")).bind("hausnummer");
		
		Eventorganisator einEventorganisator = new Eventorganisator();
		buttonHinzufuegen.addClickListener(event -> {
			try {
				binder.writeBean(einEventorganisator); // dem Objekt werden Attributwerte aus den Textfeldern (via Binder) zugewiesen
				
				iAddEventorganisatorService.speicherEventorganisator(einEventorganisator); // Uebergabe an Service zur Speicherung in DB
				notificationSavesuccess.open(); // Erfolgreich-Meldung anzeigen
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});

	}
}
