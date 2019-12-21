package de.swprojekt.speeddating.ui.untern;

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
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;
import de.swprojekt.speeddating.service.unternehmen.IUnternehmenService;
import de.swprojekt.speeddating.ui.MainLayout;

/*
 * View zum Aendern und Loeschen von Studierenden
 */
@Route(value = "ui/untern/changeDeleteUntern", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
@Secured("ROLE_EVENTORGANISATOR")
public class ChangeDeleteUntern extends VerticalLayout {

	int lv_id = 0;

	// BestPractice: Konstruktor-Injection im Vergleich zu
	// Attribut/Methoden-Injection
	// Parameter (hier: IAddStudierenderService) wird also automatisch autowired
	@Autowired
	public ChangeDeleteUntern(IUnternehmenService iUnternehmenService,
			IShowUnternehmenService iShowUnternehmenService) {
		// Deklaration

		Binder<Unternehmen> binder; // verknuepft Input aus Textfeldern mit Objektattributen
		TextField textfieldUnternehmensname = new TextField("Unternehmensname: ");
		TextField textfieldAnsprechpartner = new TextField("Anprechpartner: ");
		TextField textfieldKontaktmail = new TextField("Kontakt-EMail: ");

		// Erzeugen der Combo Box
		ComboBox<Unternehmen> comboBox = new ComboBox<>();
		comboBox.setLabel("Unternehmen auswaehlen");
		comboBox.setItemLabelGenerator(Unternehmen::getUnternehmensname);

		List<Unternehmen> listOfUnternehmen = iShowUnternehmenService.showUnternehmen();
		comboBox.setItems(listOfUnternehmen);
		comboBox.addValueChangeListener(event -> {
			Unternehmen aUnternehmen = comboBox.getValue();
			if (aUnternehmen != null) {
				lv_id = aUnternehmen.getUnternehmen_id();
				textfieldUnternehmensname.setValue(aUnternehmen.getUnternehmensname());
				textfieldAnsprechpartner.setValue(aUnternehmen.getAnsprechpartner());
				textfieldKontaktmail.setValue(aUnternehmen.getKontaktmail());
			} else {
				// message.setText("No song is selected");
			}

		});

		// Button #1 hinzufuegen
		Button buttonUnternAendern = new Button("Unternehmen aendern");
		buttonUnternAendern.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		// Button #2 hinzufuegen
		Button buttonUnternLoeschen = new Button("Unternehmen loeschen");
		buttonUnternLoeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);
		// Button #3 hinzufuegen
		Button buttonZurueck = new Button("Zurueck");
		buttonZurueck.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		Button logoutButton = new Button("Logout");

		// Notification Meldungen mit Button verknuepfen
		Notification notificationAendernsuccess = new Notification();
		notificationAendernsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelAendernsuccess = new Label("Unternehmen erfolgreich aktualisiert! ");
		notificationAendernsuccess.add(labelAendernsuccess);
		notificationAendernsuccess.setDuration(2500); //Meldung wird 2,5 Sekunden lang angezeigt

		Notification notificationLoeschensuccess = new Notification();
		notificationLoeschensuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelLoeschensuccess = new Label("Unternehmen erfolgreich geloescht! ");
		notificationLoeschensuccess.add(labelLoeschensuccess);
		notificationLoeschensuccess.setDuration(2500); //Meldung wird 2,5 Sekunden lang angezeigt

		Notification notificationAbbruch = new Notification();
		notificationAbbruch.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelAbbruchsuccess = new Label("Bearbeitung abgebrochen! ");
		notificationAbbruch.add(labelAbbruchsuccess);
		notificationAbbruch.setDuration(2500); //Meldung wird 2,5 Sekunden lang angezeigt
		
		// Bestaetigungs-Popup
		Button buttonBestaetigenJa = new Button("Ja");
		Button buttonBestaetigenNein = new Button("Nein");
		Dialog popUpBestaetigen = new Dialog();
		popUpBestaetigen.add(new Label("Das Unternehmen endgueltig loeschen?"));
		popUpBestaetigen.add(buttonBestaetigenJa, buttonBestaetigenNein);
		
		// *** Erzeugen des Layouts START ***
		VerticalLayout h1 = new VerticalLayout(); // Textfelder sollen nebeneinander angeordnet werden
		h1.add(comboBox);
		h1.add(textfieldUnternehmensname);
		h1.add(textfieldAnsprechpartner);
		h1.add(textfieldKontaktmail);

		h1.add(new HorizontalLayout(buttonUnternAendern, buttonUnternLoeschen));
		h1.add(new HorizontalLayout(buttonZurueck, logoutButton)); // darunter wird Button angeordnet
		add(h1);
		// *** Erzeugen des Layouts ENDE ***

		binder = new Binder<>(Unternehmen.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)
		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		// verknuepf

		binder.forField(textfieldUnternehmensname).asRequired("Unternehmensname darf nicht leer sein...")
				.bind("unternehmensname");
		binder.forField(textfieldAnsprechpartner).asRequired("Ansprechpartner darf nicht leer sein...")
				.bind("ansprechpartner");
		binder.forField(textfieldKontaktmail).asRequired("Kontakt-EMail darf nicht leer sein...").bind("kontaktmail");

		Unternehmen einUnternehmenTmp = new Unternehmen();
		buttonUnternAendern.addClickListener(event -> {
			try {
				binder.writeBean(einUnternehmenTmp); // dem Objekt werden Attributwerte aus den Textfeldern (via Binder)
													// zugewiesen
				Unternehmen aUnternehmen = iShowUnternehmenService.showEinUnternehmen(lv_id);
				aUnternehmen.setUnternehmensname(einUnternehmenTmp.getUnternehmensname());
				aUnternehmen.setAnsprechpartner(einUnternehmenTmp.getAnsprechpartner());
				aUnternehmen.setKontaktmail(einUnternehmenTmp.getKontaktmail());
				iUnternehmenService.changeUnternehmen(aUnternehmen);
				notificationAendernsuccess.open();
//				SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//				getUI().get().getSession().close();		//Vaadin Session leeren
				buttonUnternAendern.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf
																										// andere Seite

			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});

		buttonUnternLoeschen.addClickListener(event -> {
			popUpBestaetigen.open();
		});

		buttonBestaetigenJa.addClickListener(event -> {
			try {
				binder.writeBean(einUnternehmenTmp);
				einUnternehmenTmp.setUnternehmen_id(lv_id);
				iUnternehmenService.deleteUnternehmen(einUnternehmenTmp);
				notificationLoeschensuccess.open();
				popUpBestaetigen.close();
				buttonZurueck.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere
																									// Seite
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
			//getUI().get().getSession().close(); // Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});

	}

}
