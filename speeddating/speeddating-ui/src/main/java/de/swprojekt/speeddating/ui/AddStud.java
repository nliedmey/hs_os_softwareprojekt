package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.service.addstudierender.IStudierenderService;
import de.swprojekt.speeddating.service.security.IRegisterUserService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;

/*
 * View zum Anlegen von neuem Studierenden
 */
@Route(value = "ui/studs/add", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
@Secured("ROLE_EVENTORGANISATOR")
public class AddStud extends VerticalLayout {

	// BestPractice: Konstruktor-Injection im Vergleich zu
	// Attribut/Methoden-Injection
	// Parameter (hier: IAddStudierenderService) wird also automatisch autowired
	@Autowired
	public AddStud(IStudierenderService iAddStudierenderService, IShowStudierendeService iShowStudierendeService,
			IRegisterUserService iRegisterUserService) {

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

		// Button hinzufuegen
		Button buttonHinzufuegen = new Button("Student anlegen");
		buttonHinzufuegen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		Button zurueckbutton = new Button("Zurueck");
		zurueckbutton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		
		Button logoutButton=new Button("Logout");


		// Notification Meldungen mit Button verknuepfen
		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Student erfolgreich hinzugefuegt! ");
		notificationSavesuccess.add(labelSavesuccess);
		notificationSavesuccess.setDuration(2500); //Meldung wird 2,5 Sekunden lang angezeigt
		
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
		h1.add(buttonHinzufuegen);
		add(h1, new HorizontalLayout(zurueckbutton, logoutButton)); // darunter wird Button angeordnet

		binder = new Binder<>(Studierender.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)

		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		// verknuepf

		binder.forField(textfieldMatrikelnr).asRequired("Matrikelnummer darf nicht leer sein...")
				.withConverter(new StringToIntegerConverter("Eingabe muss numerisch sein")).bind("matrikelnummer");
		binder.forField(textfieldVorname).asRequired("Vorname darf nicht leer sein...").bind("vorname");
		binder.forField(textfieldNachname).asRequired("Nachname darf nicht leer sein...").bind("nachname");
		binder.forField(textfieldStrasse).bind("strasse");
		binder.forField(textfieldHausnummer).withConverter(new StringToIntegerConverter("Eingabe muss numerisch sein"))
				.bind("hausnummer");
		binder.forField(textfieldPLZ).bind("plz");
		binder.forField(textfieldOrt).bind("ort");
		binder.forField(textfieldTelefonnr).bind("telefonnr");
		binder.forField(textfieldEMail).asRequired("E-Mail Adresse darf nicht leer sein...").bind("email");

		Studierender einStudierender = new Studierender();

		buttonHinzufuegen.addClickListener(event -> {
			try {
				binder.writeBean(einStudierender); // dem Objekt werden Attributwerte aus den Textfeldern (via Binder)
													// zugewiesen

				Studierender neuerStud = iAddStudierenderService.saveStudierenden(einStudierender); // Uebergabe an
																									// Service zur
																									// Speicherung in DB
				iRegisterUserService.save(neuerStud.getNachname() + "_" + neuerStud.getMatrikelnummer(), "standard",
						"STUDENT", neuerStud.getStudent_id()); // Einloggbenutzer anlegen fuer den Studenten
				
				// Nutzername: Nachname_Matrikelnummer, Initialpasswort: standard
				notificationSavesuccess.open(); // Erfolgreich-Meldung anzeigen
				buttonHinzufuegen.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere Seite

			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});

		zurueckbutton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			zurueckbutton.getUI().ifPresent(ui->ui.navigate("ui/eventorganisator/menue"));	//zurueck auf andere Seite 
		});
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			//getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("login"));	//zurueck auf andere Seite 
		});

	}

}
