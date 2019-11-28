package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.service.addstudierender.IAddStudierenderService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;

/*
 * View zum Anlegen von neuem Studierenden
 */
@Route(value = "ui/studs/add", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
public class AddStud extends VerticalLayout {

	 // BestPractice: Konstruktor-Injection im Vergleich zu
				// Attribut/Methoden-Injection
				// Parameter (hier: IAddStudierenderService) wird also automatisch autowired
	@Autowired
	public AddStud(IAddStudierenderService iAddStudierenderService, IShowStudierendeService iShowStudierendeService) {

		// Deklaration
		Binder<Studierender> binder; // verknuepft Input aus Textfeldern mit Objektattributen

		// Erzeugen der Input Felder
		TextField textfieldMatrikelnr = new TextField("Matrikelnummer:");
		TextField textfieldVorname = new TextField("Vorname: ");
		TextField textfieldNachname = new TextField("Nachname: ");
		TextField textfieldStrasse = new TextField("Straße: ");
		TextField textfieldHausnummer = new TextField("Hausnummer::");
		TextField textfieldPLZ = new TextField("Postleitzahl: ");
		TextField textfieldOrt = new TextField("Ort: ");
		TextField textfieldTelefonnr = new TextField("Telefonnr.: ");
		TextField textfieldEMail = new TextField("E-Mail Adresse: ");
		
		
		ComboBox<Studierender> comboBox = new ComboBox<>();
		comboBox.setLabel("Studenten auswaehlen");
		comboBox.setItemLabelGenerator(Studierender:: getNachname);

		List<Studierender> listOfStudenten = iShowStudierendeService.showStudierende();
		comboBox.setItems(listOfStudenten);
		comboBox.addValueChangeListener(event -> {
		    Studierender student = comboBox.getValue();

		});
		
		

		// Button hinzufuegen
		Button buttonHinzufuegen = new Button("Student anlegen");
		buttonHinzufuegen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		Button buttonAbbrechen = new Button("Abbrechen");
		buttonAbbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

		// Notification Meldungen mit Button verknuepfen
		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Student erfolgreich hinzugefuegt! ");
		notificationSavesuccess.add(labelSavesuccess);
				
		VerticalLayout h1 = new VerticalLayout(); // Textfelder sollen nebeneinander angeordnet werden
		h1.add(textfieldMatrikelnr);
		h1.add(textfieldVorname);
		h1.add(textfieldNachname);		
		
		h1.add(textfieldStrasse);
		h1.add(textfieldHausnummer);
		h1.add(textfieldPLZ);
		h1.add(textfieldOrt);
		
		h1.add(textfieldTelefonnr);
		h1.add(textfieldEMail);
		
		h1.add(comboBox);
				
		add(h1, buttonHinzufuegen, buttonAbbrechen); // darunter wird Button angeordnet
		
		

		binder = new Binder<>(Studierender.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)

		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		// verknuepf

		binder.forField(textfieldMatrikelnr).asRequired("Matrikelnummer darf nicht leer sein...").withConverter(new StringToIntegerConverter("Eingabe muss numerisch sein")).bind("matrikelnummer");
		binder.forField(textfieldVorname).asRequired("Vorname darf nicht leer sein...").bind("vorname");
		binder.forField(textfieldNachname).asRequired("Nachname darf nicht leer sein...").bind("nachname");
		binder.forField(textfieldStrasse).bind("strasse");
		binder.forField(textfieldHausnummer).withConverter(new StringToIntegerConverter("Eingabe muss numerisch sein")).bind("hausnummer");
		binder.forField(textfieldPLZ).bind("plz");
		binder.forField(textfieldOrt).bind("ort");
		binder.forField(textfieldTelefonnr).bind("telefonnr");
		binder.forField(textfieldEMail).asRequired("E-Mail Adresse darf nicht leer sein...").bind("email"); 
		
		Studierender einStudierender = new Studierender();
		
		buttonHinzufuegen.addClickListener(event -> {
			try {
				binder.writeBean(einStudierender); // dem Objekt werden Attributwerte aus den Textfeldern (via Binder)
													// zugewiesen
				iAddStudierenderService.speicherStudierenden(einStudierender); // Uebergabe an Service zur Speicherung
																				// in DB
				notificationSavesuccess.open(); // Erfolgreich-Meldung anzeigen
				
		        
				
//				SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//				getUI().get().getSession().close();		//Vaadin Session leeren
			    buttonHinzufuegen.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
				
				
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});
		
		buttonAbbrechen.addClickListener(event -> {
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			buttonHinzufuegen.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});
		
		

	}

}
