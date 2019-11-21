package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.service.addstudierender.IAddStudierenderService;
/*
 * View zum Anlegen von neuem Studierenden
 */
@Route(value = "ui/studs/add", layout = MainLayout.class)	//Abgeleitet von Root-Layout MainLayout
public class AddStud extends VerticalLayout {

	@Autowired	//BestPractice: Konstruktor-Injection im Vergleich zu Attribut/Methoden-Injection
				//Parameter (hier: IAddStudierenderService) wird also automatisch autowired
	public AddStud(IAddStudierenderService iAddStudierenderService) {
		TextField textfieldVorname;
		TextField textfieldNachname;
		TextField textfieldHauptfach;
		Button buttonHinzufuegen;
		Notification notificationSavesuccess;
		Label labelSavesuccess;

		Binder<Studierender> binder;	//verknuepft Input aus Textfeldern mit Objektattributen

		textfieldVorname = new TextField("Vorname: ");
		textfieldNachname = new TextField("Nachname: ");
		textfieldHauptfach = new TextField("Hauptfach: ");

		buttonHinzufuegen = new Button("Student hinzufuegen");
		buttonHinzufuegen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

		labelSavesuccess = new Label("Student erfolgreich hinzugefuegt! ");
		notificationSavesuccess.add(labelSavesuccess);

		binder = new Binder<>(Studierender.class);	//Klasse fuer Binder festlegen (kennt somit Objektattribute)
		binder.forField(textfieldVorname).asRequired("Vorname darf nicht leer sein...").bind("vorname");	//textfieldVorname wird mit Objektattribut "vorname" verknuept
		binder.forField(textfieldNachname).asRequired("Nachname darf nicht leer sein...").bind("nachname");	//zusaetzlich wird leere Angabe beanstandet
		binder.forField(textfieldHauptfach).asRequired("Hauptfach darf nicht leer sein...").bind("hauptfach");

		Studierender einStudierender = new Studierender();
		buttonHinzufuegen.addClickListener(event -> {
			try {
				binder.writeBean(einStudierender);	//Objekt werden Attributwerte aus Textfeldern (via Binder) zugewiesen
				iAddStudierenderService.speicherStudierenden(einStudierender);	//Uebergabe an Service zur Speicherung in DB
				notificationSavesuccess.open();	//Erfolgreich-Meldung anzeigen
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});

		HorizontalLayout h1=new HorizontalLayout();	//Textfelder sollen nebeneinander angeordnet werden
		h1.add(textfieldVorname);
		h1.add(textfieldNachname);
		h1.add(textfieldHauptfach);
		add(h1,buttonHinzufuegen);	//darunter wird Button angeordnet
	}
}
