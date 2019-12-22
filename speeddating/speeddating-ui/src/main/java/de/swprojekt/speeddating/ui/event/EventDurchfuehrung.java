package de.swprojekt.speeddating.ui.event;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.alterevent.IAlterEventService;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;

/*
 * View fuer die Durchfuehrung des Events
 */

@Route("ui/eventDurchfuehrung") // Erreichbar ueber Adresse:
								// //localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/events
@Secured("ROLE_EVENTORGANISATOR")
public class EventDurchfuehrung extends HorizontalLayout {

	// Deklarationen
	int anzahlRunden;
	int anzahlStuds;
	int anzahlUntern;
	int tempRunde;
	int lv_id = 0;
	int geplanteRundenzeit = 0;

	boolean timerLaeuft = false;

	long secGepl;
	long s;
	long m;
	String time;

	Clip clip;
	Event aEvent;

	@SuppressWarnings("deprecation")
	@Autowired
	public EventDurchfuehrung(IShowEventService iShowEventService, IShowUnternehmenService iShowUnternehmenService,
			IShowStudierendeService iShowStudierendeService, IAlterEventService iAlterEventService) {

		Notification notificationEventBeendetsuccess = new Notification();
		notificationEventBeendetsuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelBeendetGeklicktsuccess = new Label("Event erfolgreich beendet! ");
		notificationEventBeendetsuccess.add(labelBeendetGeklicktsuccess);
		notificationEventBeendetsuccess.setDuration(2500); // Meldung wird 2,5 Sekunden lang angezeigt

		// Buttons und Felder erzeugen
		Label labelTabelle = new Label("Tabelle");
		Label labelRunde = new Label("Aktuelle Runde:");
		Label labelRundeAnzahl = new Label("1");
		Label labelMaxRunden = new Label("");

		Label labelTimer = new Label();
		Label labelTischordnung = new Label("Tischordnung:");

		Label labelTisch1 = new Label("Tisch 1:");
		Label labelTisch2 = new Label("Tisch 2:");
		Label labelTisch3 = new Label("Tisch 3:");
		Label labelTisch4 = new Label("Tisch 4:");
		Label labelTisch5 = new Label("Tisch 5:");
		Label labelTisch6 = new Label("Tisch 6:");
		Label labelTisch7 = new Label("Tisch 7:");
		Label labelTisch8 = new Label("Tisch 8:");
		Label labelTisch9 = new Label("Tisch 9:");
		Label labelTisch10 = new Label("Tisch 10:");
		Label labelTisch11 = new Label("Tisch 11:");
		Label labelTisch12 = new Label("Tisch 12:");

		Label labelStud1 = new Label("");
		Label labelStud2 = new Label("");
		Label labelStud3 = new Label("");
		Label labelStud4 = new Label("");
		Label labelStud5 = new Label("");
		Label labelStud6 = new Label("");
		Label labelStud7 = new Label("");
		Label labelStud8 = new Label("");
		Label labelStud9 = new Label("");
		Label labelStud10 = new Label("");
		Label labelStud11 = new Label("");
		Label labelStud12 = new Label("");

		Label labelUnter1 = new Label("");
		Label labelUnter2 = new Label("");
		Label labelUnter3 = new Label("");
		Label labelUnter4 = new Label("");
		Label labelUnter5 = new Label("");
		Label labelUnter6 = new Label("");
		Label labelUnter7 = new Label("");
		Label labelUnter8 = new Label("");
		Label labelUnter9 = new Label("");
		Label labelUnter10 = new Label("");
		Label labelUnter11 = new Label("");
		Label labelUnter12 = new Label("");

		Button buttonEventauswahl = new Button("Event auswaehlen");
		Button zurueckButton = new Button("Zurueck");
		Button buttonStart = new Button("Start Timer");
		Button buttonNaechsteRunde = new Button("Naechste Runde");
		Button buttonPauseFortsetzen = new Button("Pause / Fortsetzen");
		Button buttonBeenden = new Button("Beenden");

		initializing();

		// Erzeugen der Combo Box
		ComboBox<Event> comboBox = new ComboBox<>();
		comboBox.setItemLabelGenerator(Event::getBezeichnung);
		comboBox.setPlaceholder("Event auswaehlen");
		List<Event> listOfEvents = new ArrayList<>();

		// Alle Events des angemeldeten Organisators laden
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal(); // Id des eingeloggten Users aus SecurityKontext holen

		for (int event_id : iShowEventService.showEventsOfUser(userDetails.getEntityRefId())) // alle Events, an welchen
																								// der User beteiligt
																								// ist, laden
		{

			listOfEvents.add(iShowEventService.showEvent(event_id));

		}

		List<Event> listOfEventsLoeschen = new ArrayList<>();
		// Abgeschlossene Events aus Liste entfernen
		if (!listOfEvents.isEmpty()) {
			for (Event aEvent : listOfEvents) {
				if (aEvent.isAbgeschlossen()) {
					listOfEventsLoeschen.add(aEvent);
				}
			}
			listOfEvents.removeAll(listOfEventsLoeschen);
			comboBox.setItems(listOfEvents);
			if (listOfEvents.isEmpty()) {
				comboBox.setPlaceholder("Keine Events");
			}
		} else {
			comboBox.setPlaceholder("Keine Events");
		}

		// Labels in Arrays speichern um spaetere Befuellung zu vereinfachen
		List<Label> labelsStuds = Arrays.asList(labelStud1, labelStud2, labelStud3, labelStud4, labelStud5, labelStud6,
				labelStud7, labelStud8, labelStud9, labelStud10, labelStud11, labelStud12);
		List<Label> labelsUntern = Arrays.asList(labelUnter1, labelUnter2, labelUnter3, labelUnter4, labelUnter5,
				labelUnter6, labelUnter7, labelUnter8, labelUnter9, labelUnter10, labelUnter11, labelUnter12);

		// Listen
		List<Unternehmen> listeUntern = new ArrayList();
		List<Studierender> listeStuds = new ArrayList();

		// Pooling zunächst deaktivieren fuer Timer
		UI.getCurrent().setPollInterval(-1);

		// SoundPlayer fuer Signalton
		try {
			clip = AudioSystem.getClip();
			//unten fuer Webdeploy
//			String directory=System.getProperty("jboss.server.data.dir"); //Property verweist auf Datenverzeichnes des Wildflyservers (auf Server: opt/wildfly)
//			String filename="WAV001.WAV";
//			String filepath = directory+"\\otherFiles\\"+filename;
//			URL link = this.getClass().getResource(filepath);
			URL link = this.getClass().getResource("./WAV001.WAV");
			System.out.println(link);
			File file = new File(link.getPath());
			AudioInputStream audio = AudioSystem.getAudioInputStream(file);
			clip.open(audio);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {

			e.printStackTrace();
		}

		// Layouts

		// Timer-Popup
		Button buttonClose = new Button("Close");
		Dialog popUpClose = new Dialog();
		popUpClose.add(new Label("Zeit abgelaufen!"));
		popUpClose.add(buttonClose);

		// Bestaetigungs-Popup
		Button buttonBestaetigenJa = new Button("Ja");
		Button buttonBestaetigenNein = new Button("Nein");
		Dialog popUpBestaetigen = new Dialog();
		popUpBestaetigen.add(new Label("Das Event endgueltig beenden?"));
		popUpBestaetigen.add(buttonBestaetigenJa, buttonBestaetigenNein);

		// Links
		VerticalLayout vLinks = new VerticalLayout();
		HorizontalLayout h1 = new HorizontalLayout();
		HorizontalLayout h2 = new HorizontalLayout();
		HorizontalLayout h3 = new HorizontalLayout();
		HorizontalLayout h4 = new HorizontalLayout();
		HorizontalLayout h5 = new HorizontalLayout();
		HorizontalLayout h6 = new HorizontalLayout();
		HorizontalLayout h7 = new HorizontalLayout();
		HorizontalLayout h8 = new HorizontalLayout();
		HorizontalLayout h9 = new HorizontalLayout();
		HorizontalLayout h10 = new HorizontalLayout();
		HorizontalLayout h11 = new HorizontalLayout();
		HorizontalLayout h12 = new HorizontalLayout();

		h1.add(labelTisch1, labelUnter1, labelStud1);
		h2.add(labelTisch2, labelUnter2, labelStud2);
		h3.add(labelTisch3, labelUnter3, labelStud3);
		h4.add(labelTisch4, labelUnter4, labelStud4);
		h5.add(labelTisch5, labelUnter5, labelStud5);
		h6.add(labelTisch6, labelUnter6, labelStud6);
		h7.add(labelTisch7, labelUnter7, labelStud7);
		h8.add(labelTisch8, labelUnter8, labelStud8);
		h9.add(labelTisch9, labelUnter9, labelStud9);
		h10.add(labelTisch10, labelUnter10, labelStud10);
		h11.add(labelTisch11, labelUnter11, labelStud11);
		h12.add(labelTisch12, labelUnter12, labelStud12);

		List<HorizontalLayout> listeLayouts = Arrays.asList(h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12);

		// Tische zunaechst unsichtbar
		for (HorizontalLayout h : listeLayouts) {
			h.setVisible(false);
		}

		vLinks.add(labelTischordnung, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12);

		// Rechts
		VerticalLayout vRechts = new VerticalLayout();
		vRechts.add(new HorizontalLayout(comboBox, buttonEventauswahl, zurueckButton));
		vRechts.add(new HorizontalLayout(labelRunde, labelRundeAnzahl, labelMaxRunden));
		vRechts.add(labelTimer, buttonStart, buttonPauseFortsetzen);
		vRechts.add(new HorizontalLayout(buttonNaechsteRunde, buttonBeenden));

		// Zunaechst unsichtbar. Werden nach Eventauswahl sichtbar
		buttonStart.setVisible(false);
		buttonNaechsteRunde.setVisible(false);
		buttonPauseFortsetzen.setVisible(false);
		buttonBeenden.setVisible(false);
		labelRunde.setVisible(false);
		labelRundeAnzahl.setVisible(false);
		labelMaxRunden.setVisible(false);
		labelTimer.setVisible(false);

		add(vLinks, vRechts);

		zurueckButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			zurueckButton.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere Seite
		});

		// Buttons-Funktionen
		buttonEventauswahl.addClickListener(event -> {
			this.aEvent = comboBox.getValue();
			if (aEvent != null) {
				lv_id = aEvent.getEvent_id();
				Event selectedEvent = iShowEventService.showEvent(lv_id);
				// listeUntern.addAll(selectedEvent.getTeilnehmendeUnternehmen());
				// IDs aller teilnehmen Unternehmen + Studenten
				Collection<Integer> listStudentenInEvent = new ArrayList<>(
						iShowEventService.showEvent(selectedEvent.getEvent_id()).getTeilnehmendeStudierende());
				Collection<Integer> listUnternehmenInEvent = new ArrayList<>(
						iShowEventService.showEvent(selectedEvent.getEvent_id()).getTeilnehmendeUnternehmen());

				// Objekte aus IDs erzeugen
				for (int i : listStudentenInEvent) {
					listeStuds.add(iShowStudierendeService.showStudierenden(i));
				}
				for (int j : listUnternehmenInEvent) {
					listeUntern.add(iShowUnternehmenService.showEinUnternehmen(j));
				}

				// Schleife zum erstmaligen Beschriften der Felder
				int i = 0;
				if (listeStuds.size() == listeUntern.size()) {
					while (i < listeUntern.size()) {
						listeLayouts.get(i).setVisible(true);
						labelsUntern.get(i).setText(listeUntern.get(i).getUnternehmensname());
						labelsStuds.get(i)
								.setText(listeStuds.get(i).getNachname() + ", " + listeStuds.get(i).getVorname());
						i++;
					}

				}

				// Timer starten
				geplanteRundenzeit = aEvent.getRundendauerInMinuten();
				secGepl = geplanteRundenzeit * 60;

				time = String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(secGepl),
						TimeUnit.SECONDS.toSeconds(secGepl) % TimeUnit.MINUTES.toSeconds(1));
				labelTimer.setText(time);

				// Elemente (un)visible machen
				comboBox.setVisible(false);
				buttonEventauswahl.setVisible(false);
				zurueckButton.setVisible(false);
				buttonStart.setVisible(true);
				buttonNaechsteRunde.setVisible(true);
				buttonPauseFortsetzen.setVisible(true);
				buttonBeenden.setVisible(true);
				labelRunde.setVisible(true);
				labelRundeAnzahl.setVisible(true);
				labelMaxRunden.setVisible(true);
				labelMaxRunden.setText("/ " + Integer.toString(listeStuds.size()));
				labelTimer.setVisible(true);

				// Button nachsteRunde zunächst disabled. Wird enabled, nachdem Zeit abgelaufen
				// ist
				buttonNaechsteRunde.setEnabled(false);
			}
		});

		// Startet den Timer
		buttonStart.addClickListener(event -> {

			buttonStart.setEnabled(false);
			// Polling
			UI.getCurrent().setPollInterval(1000);

		});

		// Zaehlt den Timer jede Sekunde um 1 herunter
		UI.getCurrent().addPollListener(event -> {

			timerLaeuft = true;
			if (secGepl > 0) {
				// Wenn Zeit noch nicht abgelaufen ist...
				secGepl = secGepl - 1;
				time = String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(secGepl),
						TimeUnit.SECONDS.toSeconds(secGepl) % TimeUnit.MINUTES.toSeconds(1));
				labelTimer.setText(time);
			} else {
				// Zeit ist abgelaufen...
				UI.getCurrent().setPollInterval(-1);
				// Startet den Ton
				clip.start();
				popUpClose.open();
			}

		});

		// Timer pausieren oder fortsetzen
		buttonPauseFortsetzen.addClickListener(event -> {

			if (timerLaeuft == true) {
				UI.getCurrent().setPollInterval(-1);
				timerLaeuft = false;
			} else {
				UI.getCurrent().setPollInterval(1000);
				timerLaeuft = true;
			}

		});

		buttonNaechsteRunde.addClickListener(event -> {

			anzahlRunden = Integer.parseInt(labelRundeAnzahl.getText());
			anzahlRunden = anzahlRunden + 1;

			// Pruefen, ob alle Runden gelaufen sind
			if (anzahlRunden <= listeStuds.size()) {
				labelRundeAnzahl.setText(Integer.toString(anzahlRunden));

				Studierender ersterStud = listeStuds.get(0);
				listeStuds.remove(0);
				listeStuds.add(ersterStud);

				// Hier Schleife für Befuellung der Labels der Studenten
				int i = 0;
				while (i < listeStuds.size()) {
					labelsStuds.get(i).setText(listeStuds.get(i).getNachname() + ", " + listeStuds.get(i).getVorname());
					i++;
				}
				if (anzahlRunden == listeStuds.size()) {
					buttonNaechsteRunde.setEnabled(false);
				}
			} else {
				// buttonNaechsteRunde.setEnabled(false);
			}

			// Zeit wieder aus max setzen
			geplanteRundenzeit = aEvent.getRundendauerInMinuten();
			secGepl = geplanteRundenzeit * 60;

			time = String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(secGepl),
					TimeUnit.SECONDS.toSeconds(secGepl) % TimeUnit.MINUTES.toSeconds(1));
			labelTimer.setText(time);

			// Button wieder disbale
			buttonNaechsteRunde.setEnabled(false);
			// Timer-Buttons enable
			buttonStart.setEnabled(true);
			buttonPauseFortsetzen.setEnabled(true);

		});

		buttonClose.addClickListener(event -> {
			popUpClose.close();
			buttonNaechsteRunde.setEnabled(true);
			buttonStart.setEnabled(false);
			buttonPauseFortsetzen.setEnabled(false);
		});

		buttonBeenden.addClickListener(event -> {
			popUpBestaetigen.open();
		});

		buttonBestaetigenJa.addClickListener(event -> {
			aEvent.setAbgeschlossen(true);
			iAlterEventService.aenderEvent(aEvent);
			notificationEventBeendetsuccess.open();
			buttonBeenden.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue"));
			popUpBestaetigen.close();
		});

		buttonBestaetigenNein.addClickListener(event -> {
			popUpBestaetigen.close();
		});

	}

	private void initializing() {

		anzahlRunden = 0;
		anzahlStuds = 0;
		anzahlUntern = 0;
		tempRunde = 0;
		lv_id = 0;
		geplanteRundenzeit = 0;
		timerLaeuft = false;
		secGepl = 0;
		s = 0;
		m = 0;

	}

}