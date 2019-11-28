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
 * View zum Aendern und Loeschen von Studierenden
 */
@Route(value = "ui/studs/changeDeleteStud", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
public class ChangeDeleteStud extends VerticalLayout {

	 // BestPractice: Konstruktor-Injection im Vergleich zu
				// Attribut/Methoden-Injection
				// Parameter (hier: IAddStudierenderService) wird also automatisch autowired
	@Autowired
	public ChangeDeleteStud(IAddStudierenderService iAddStudierenderService, IShowStudierendeService iShowStudierendeService) {
		// Deklaration
		
		Binder<Studierender> binder; // verknuepft Input aus Textfeldern mit Objektattributen		
		TextField textfieldMatrikelnr = new TextField("Matrikelnummer:");
		TextField textfieldVorname = new TextField("Vorname: ");
		TextField textfieldNachname = new TextField("Nachname: ");
		TextField textfieldStrasse = new TextField("Straße: ");
		TextField textfieldHausnummer = new TextField("Hausnummer::");
		TextField textfieldPLZ = new TextField("Postleitzahl: ");
		TextField textfieldOrt = new TextField("Ort: ");
		TextField textfieldTelefonnr = new TextField("Telefonnr.: ");
		TextField textfieldEMail = new TextField("E-Mail Adresse: ");

		// Erzeugen der Combo Box		
		ComboBox<Studierender> comboBox = new ComboBox<>();
		comboBox.setLabel("Studenten auswaehlen");
		comboBox.setItemLabelGenerator(Studierender:: getStringFullNameOfStudent);

		List<Studierender> listOfStudenten = iShowStudierendeService.showStudierende();
		comboBox.setItems(listOfStudenten);
		comboBox.addValueChangeListener(event -> {
		    Studierender aStudent = comboBox.getValue();
		    if (aStudent != null) {
		    	
		    	textfieldMatrikelnr.setValue(Integer.toString(aStudent.getMatrikelnummer()));
		    	textfieldVorname.setValue(aStudent.getVorname());
		    	textfieldNachname.setValue(aStudent.getNachname());
		    	textfieldStrasse.setValue(aStudent.getStrasse());
		    	textfieldHausnummer.setValue(Integer.toString(aStudent.getHausnummer()));
		    	textfieldPLZ.setValue(aStudent.getPlz());
		    	textfieldOrt.setValue(aStudent.getOrt());
		    	textfieldTelefonnr.setValue(aStudent.getTelefonnr());
		    	textfieldEMail.setValue(aStudent.getEmail());
		    } else {
		    	//message.setText("No song is selected");
		    }

		});
		
		// Button #1 hinzufuegen
		Button buttonStudAendern = new Button("Student aendern");
		buttonStudAendern.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		// Button #2 hinzufuegen
		Button buttonStudLoeschen = new Button("Student loeschen");
		buttonStudLoeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);
		// Button #3 hinzufuegen
		Button buttonAbbrechen = new Button("Abbrechen");
		buttonAbbrechen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		// Notification Meldungen mit Button verknuepfen
		Notification notificationAendernsuccess = new Notification();
		notificationAendernsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelAendernsuccess = new Label("Student erfolgreich aktualisiert! ");
		notificationAendernsuccess.add(labelAendernsuccess);
		
		Notification notificationLoeschensuccess = new Notification();
		notificationLoeschensuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelLoeschensuccess = new Label("Student erfolgreich geloescht! ");
		notificationLoeschensuccess.add(labelLoeschensuccess);
		
		Notification notificationAbbruch = new Notification();
		notificationAbbruch.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelAbbruchsuccess = new Label("Studentbearbeitung abgebrochen! ");
		notificationAbbruch.add(labelAbbruchsuccess);		
		
		
		//*** Erzeugen des Layouts START *** 				
		VerticalLayout h1 = new VerticalLayout(); // Textfelder sollen nebeneinander angeordnet werden
		h1.add(comboBox);
		h1.add(textfieldMatrikelnr);
		h1.add(textfieldVorname);
		h1.add(textfieldNachname);		
		
		h1.add(textfieldStrasse);
		h1.add(textfieldHausnummer);
		h1.add(textfieldPLZ);
		h1.add(textfieldOrt);
		
		h1.add(textfieldTelefonnr);
		h1.add(textfieldEMail);

		add(h1, buttonStudAendern, buttonStudLoeschen, buttonAbbrechen); // darunter wird Button angeordnet
		//*** Erzeugen des Layouts ENDE *** 	


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
		buttonStudAendern.addClickListener(event -> {
			try {
				binder.writeBean(einStudierender); // dem Objekt werden Attributwerte aus den Textfeldern (via Binder) zugewiesen
													

				iAddStudierenderService.speicherStudierenden(einStudierender);
				
				notificationAendernsuccess.open();
//				SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//				getUI().get().getSession().close();		//Vaadin Session leeren
			    buttonStudAendern.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
				
				
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});
		
		buttonAbbrechen.addClickListener(event -> {
			// Erfolgreich-Meldung anzeigen
			notificationAbbruch.open();
			
//			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//			getUI().get().getSession().close();		//Vaadin Session leeren
			buttonAbbrechen.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});
		
		buttonStudLoeschen.addClickListener(event -> {
			// Erfolgreich-Meldung anzeigen
			notificationLoeschensuccess.open();
			
//			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//			getUI().get().getSession().close();		//Vaadin Session leeren
			buttonAbbrechen.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});

	}

}
