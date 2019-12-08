package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;
import de.swprojekt.speeddating.service.unternehmen.IUnternehmenService;

/*
 * View zum Aendern und Loeschen von Studierenden
 */
@Route(value = "ui/untern/changeDeleteUntern", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
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
		Button buttonAbbrechen = new Button("Abbrechen");
		buttonAbbrechen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		// Notification Meldungen mit Button verknuepfen
		Notification notificationAendernsuccess = new Notification();
		notificationAendernsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelAendernsuccess = new Label("Unternehmen erfolgreich aktualisiert! ");
		notificationAendernsuccess.add(labelAendernsuccess);

		Notification notificationLoeschensuccess = new Notification();
		notificationLoeschensuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelLoeschensuccess = new Label("Unternehmen erfolgreich geloescht! ");
		notificationLoeschensuccess.add(labelLoeschensuccess);

		Notification notificationAbbruch = new Notification();
		notificationAbbruch.addThemeVariants(NotificationVariant.LUMO_ERROR);
		Label labelAbbruchsuccess = new Label("Bearbeitung abgebrochen! ");
		notificationAbbruch.add(labelAbbruchsuccess);

		// *** Erzeugen des Layouts START ***
		VerticalLayout h1 = new VerticalLayout(); // Textfelder sollen nebeneinander angeordnet werden
		h1.add(comboBox);
		h1.add(textfieldUnternehmensname);
		h1.add(textfieldAnsprechpartner);
		h1.add(textfieldKontaktmail);

		add(h1, buttonUnternAendern, buttonUnternLoeschen, buttonAbbrechen); // darunter wird Button angeordnet
		// *** Erzeugen des Layouts ENDE ***

		binder = new Binder<>(Unternehmen.class); // Klasse fuer Binder festlegen (kennt somit Objektattribute)
		// Musseingaben definieren textfieldXXX wird mit Objektattribut "xxx" verknuepft
		// verknuepf

		binder.forField(textfieldUnternehmensname).asRequired("Unternehmensname darf nicht leer sein...").bind("unternehmensname");
		binder.forField(textfieldAnsprechpartner).asRequired("Ansprechpartner darf nicht leer sein...").bind("ansprechpartner");
		binder.forField(textfieldKontaktmail).asRequired("Kontakt-EMail darf nicht leer sein...").bind("kontaktmail");

		Unternehmen einUnternehmen = new Unternehmen();
		buttonUnternAendern.addClickListener(event -> {
			try {
				binder.writeBean(einUnternehmen); // dem Objekt werden Attributwerte aus den Textfeldern (via Binder)
													// zugewiesen
				einUnternehmen.setUnternehmen_id(lv_id);
				iUnternehmenService.changeUnternehmen(einUnternehmen);
				notificationAendernsuccess.open();
//				SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//				getUI().get().getSession().close();		//Vaadin Session leeren
				buttonUnternAendern.getUI().ifPresent(ui -> ui.navigate("maincontent")); // zurueck auf andere Seite

			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});

		buttonUnternLoeschen.addClickListener(event -> {

			try {

				binder.writeBean(einUnternehmen);
				einUnternehmen.setUnternehmen_id(lv_id);
				iUnternehmenService.deleteUnternehmen(einUnternehmen);
				notificationLoeschensuccess.open();
//			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//			getUI().get().getSession().close();		//Vaadin Session leeren
				buttonAbbrechen.getUI().ifPresent(ui -> ui.navigate("maincontent")); // zurueck auf andere Seite

			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});
		
		buttonAbbrechen.addClickListener(event -> {
			// Erfolgreich-Meldung anzeigen
			notificationAbbruch.open();
//			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//			getUI().get().getSession().close();		//Vaadin Session leeren
			buttonAbbrechen.getUI().ifPresent(ui -> ui.navigate("maincontent")); // zurueck auf andere Seite
		});

	}

}
