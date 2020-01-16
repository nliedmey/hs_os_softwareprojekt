package de.swprojekt.speeddating.ui.stud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.util.List;
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

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.service.addstudierender.IStudierenderService;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.ui.MainLayout;

/*
 * View zum Aendern und Loeschen von Studierenden
 */
@Route(value = "ui/studs/changeDeleteStud", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
@Secured("ROLE_EVENTORGANISATOR")
public class ChangeDeleteStud extends VerticalLayout {

	int lv_id = 0;

	// BestPractice: Konstruktor-Injection im Vergleich zu
	// Attribut/Methoden-Injection
	// Parameter (hier: IAddStudierenderService) wird also automatisch autowired
	@Autowired
	public ChangeDeleteStud(IStudierenderService iStudierenderService,
			IShowStudierendeService iShowStudierendeService, IShowEventService iShowEventService) {
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
		comboBox.setItemLabelGenerator(Studierender::getStringFullNameOfStudent);

		List<Studierender> listOfStudenten = iShowStudierendeService.showStudierende();
		comboBox.setItems(listOfStudenten);
		comboBox.addValueChangeListener(event -> {
			Studierender aStudent = comboBox.getValue();
			if (aStudent != null) {
				lv_id = aStudent.getStudent_id();
				textfieldMatrikelnr.setValue(Integer.toString(aStudent.getMatrikelnummer()));
				textfieldVorname.setValue(aStudent.getVorname());
				textfieldNachname.setValue(aStudent.getNachname());
				textfieldStrasse.setValue(aStudent.getStrasse());
				textfieldHausnummer.setValue(aStudent.getHausnummer());
				textfieldPLZ.setValue(aStudent.getPlz());
				textfieldOrt.setValue(aStudent.getOrt());
				textfieldTelefonnr.setValue(aStudent.getTelefonnr());
				textfieldEMail.setValue(aStudent.getEmail());
			} else {
				// message.setText("No song is selected");
			}

		});

		// Button #1 hinzufuegen
		Button buttonStudAendern = new Button("Student aendern");
		buttonStudAendern.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		// Button #2 hinzufuegen
		Button buttonStudLoeschen = new Button("Student loeschen");
		buttonStudLoeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);
		// Button #3 hinzufuegen
		Button buttonZurueck = new Button("Zurueck");
		buttonZurueck.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		Button logoutButton = new Button("Logout");

		// Notification Meldungen mit Button verknuepfen
		Notification notificationAendernsuccess = new Notification();
		notificationAendernsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelAendernsuccess = new Label("Student erfolgreich aktualisiert! ");
		notificationAendernsuccess.add(labelAendernsuccess);
		notificationAendernsuccess.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		Notification notificationLoeschensuccess = new Notification();
		notificationLoeschensuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelLoeschensuccess = new Label("Student erfolgreich geloescht! ");
		notificationLoeschensuccess.add(labelLoeschensuccess);
		notificationLoeschensuccess.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		Notification notificationAbbruch = new Notification();
		notificationAbbruch.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelAbbruchsuccess = new Label("Studentbearbeitung abgebrochen! ");
		notificationAbbruch.add(labelAbbruchsuccess);
		notificationAbbruch.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt
		
		Notification notificationNotPossible = new Notification();
		notificationNotPossible.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelNotPossiblesuccess = new Label("Student kann nicht geloescht werden, da Eventzuordnungen vorhanden! ");
		notificationNotPossible.add(labelNotPossiblesuccess);
		notificationNotPossible.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		// Bestaetigungs-Popup
		Button buttonBestaetigenJa = new Button("Ja");
		Button buttonBestaetigenNein = new Button("Nein");
		Dialog popUpBestaetigen = new Dialog();
		popUpBestaetigen.add(new Label("Den Studierenden endgueltig loeschen?"));
		popUpBestaetigen.add(buttonBestaetigenJa, buttonBestaetigenNein);

		// *** Erzeugen des Layouts START ***
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

		h1.add(new HorizontalLayout(buttonStudAendern, buttonStudLoeschen));
		h1.add(new HorizontalLayout(buttonZurueck, logoutButton)); // darunter wird Button
		add(h1); // angeordnet
		// *** Erzeugen des Layouts ENDE ***

		binder = new Binder<>(Studierender.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)
		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		// verknuepf

		binder.forField(textfieldMatrikelnr).asRequired("Matrikelnummer darf nicht leer sein...")
				.withConverter(new StringToIntegerConverter("Eingabe muss numerisch sein")).bind("matrikelnummer");
		binder.forField(textfieldVorname).asRequired("Vorname darf nicht leer sein...").bind("vorname");
		binder.forField(textfieldNachname).asRequired("Nachname darf nicht leer sein...").bind("nachname");
		binder.forField(textfieldStrasse).bind("strasse");
		binder.forField(textfieldHausnummer).bind("hausnummer");
		binder.forField(textfieldPLZ).bind("plz");
		binder.forField(textfieldOrt).bind("ort");
		binder.forField(textfieldTelefonnr).bind("telefonnr");
		binder.forField(textfieldEMail).asRequired("E-Mail Adresse darf nicht leer sein...").bind("email");

		Studierender studtmp = new Studierender();
		buttonStudAendern.addClickListener(event -> {
			try {
				binder.writeBean(studtmp); // dem Objekt werden Attributwerte aus den Textfeldern (via Binder)
											// zugewiesen

				Studierender einStudierender = iShowStudierendeService.showStudierenden(lv_id);
				einStudierender.setMatrikelnummer(studtmp.getMatrikelnummer());
				einStudierender.setVorname(studtmp.getVorname());
				einStudierender.setNachname(studtmp.getNachname());
				einStudierender.setStrasse(studtmp.getStrasse());
				einStudierender.setHausnummer(studtmp.getHausnummer());
				einStudierender.setPlz(studtmp.getPlz());
				einStudierender.setOrt(studtmp.getOrt());
				einStudierender.setTelefonnr(studtmp.getTelefonnr());
				einStudierender.setEmail(studtmp.getEmail());

				iStudierenderService.changeStudierenden(einStudierender);
				notificationAendernsuccess.open();
//				SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//				getUI().get().getSession().close();		//Vaadin Session leeren
				buttonStudAendern.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf
																										// andere Seite
				// }
				// }
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});

		buttonStudLoeschen.addClickListener(event -> {

//			try {
//				binder.writeBean(einStudierender);
//				einStudierender.setStudent_id(lv_id);
//				iStudierenderService.deleteStudierenden(einStudierender);
//				notificationLoeschensuccess.open();
////			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
////			getUI().get().getSession().close();		//Vaadin Session leeren
//				buttonZurueck.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere Seite
//			} catch (ValidationException e) {
//				e.printStackTrace();
//			}

			popUpBestaetigen.open();
		});

		buttonBestaetigenJa.addClickListener(event -> {
			try {
				binder.writeBean(studtmp);
				studtmp.setStudent_id(lv_id);

				boolean isDeletable = true;

				CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal(); //ID des eingeloggten Users aus SecurityKontext holen
				
				for (int event_id : iShowEventService.showEventsOfUser(userDetails.getEntityRefId())) {
					Event selectedEvent = iShowEventService.showEvent(event_id);
					for (Integer student_id: selectedEvent.getTeilnehmendeStudierende()) {
						if (student_id == lv_id) {
							System.out.println("Student kann nicht geloescht werden, da er in einem Event vorhanden ist");
							isDeletable = false;
						}
					}
				}

				if (isDeletable == true) {
					iStudierenderService.deleteStudierenden(studtmp);
					notificationLoeschensuccess.open();
					popUpBestaetigen.close();
					buttonZurueck.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf Seite
				} else {
					notificationNotPossible.open();	
					popUpBestaetigen.close();
					buttonZurueck.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue"));
				}
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});

		buttonBestaetigenNein.addClickListener(event -> {
			popUpBestaetigen.close();
		});

		buttonZurueck.addClickListener(event -> {
			// Erfolgreich-Meldung anzeigen
			notificationAbbruch.open();
			buttonZurueck.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere Seite

		});

		logoutButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
			// getUI().get().getSession().close(); // Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});
	}

}
