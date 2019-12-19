package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.addstudierender.IStudierenderService;
import de.swprojekt.speeddating.service.security.CustomUserDetails;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;
import de.swprojekt.speeddating.service.unternehmen.IUnternehmenService;
/*
 * View fuer die Anzeige vorhandener Events
 */

@Route("ui/eventVotingView_Stud") 
@Secured("ROLE_STUDENT")	//nur User mit Rolle STUDENT koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class EventVotingView_Stud extends VerticalLayout { // VerticalLayout fuehrt zu Anordnung von Elementen
															// untereinander statt nebeneinander (HorizontalLayout)

	@Autowired // Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public EventVotingView_Stud(IShowEventService iShowEventService, IShowUnternehmenService iShowUnternehmenService,
			IUnternehmenService iUnternehmenService, IShowStudierendeService iShowStudierendeService,
			IStudierenderService iStudierenderService) {

		Grid<Unternehmen> unternehmenGrid; // Tabelle mit Events
		unternehmenGrid = new Grid<>(Unternehmen.class); // Tabelle initialisieren
		GridMultiSelectionModel<Unternehmen> selectionModelUnternehmen;
		List<Unternehmen> listOfUnternehmenForDisplay = new ArrayList<Unternehmen>();
		Button votingSendenButton = new Button("Kontaktwuensche absenden");
		Button logoutButton = new Button("Logout");

		// Erzeugen der Combo Box
		ComboBox<Event> comboBox = new ComboBox<>();
		comboBox.setLabel("Event auswaehlen");
		comboBox.setItemLabelGenerator(Event::getBezeichnung);
		List<Event> listOfEvents = new ArrayList<Event>();
		
		CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	//Id des eingeloggten Users aus SecurityKontext holen
		
		for(int event_id:iShowEventService.showEventsOfUser(userDetails.getEntityRefId()))	//alle Events, an welchen der User beteiligt ist, laden
		{
			listOfEvents.add(iShowEventService.showEvent(event_id));
		}
		comboBox.setDataProvider(new ListDataProvider<>(listOfEvents));	//Liste dient als Datanquelle fuer ComboBox
		comboBox.setValue(listOfEvents.get(0)); //ein Event ist standardmaessig ausgewaehlt (i.d.R. existiert auch nur eins je Student)

		Notification notificationVotingSuccess = new Notification();
		notificationVotingSuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelVotingsuccess = new Label("Voting erfolgreich abgegeben! ");
		notificationVotingSuccess.add(labelVotingsuccess);
		notificationVotingSuccess.setDuration(2500); //Meldung wird 2,5 Sekunden lang angezeigt

		ListDataProvider<Unternehmen> ldpEvent = DataProvider.ofCollection(listOfUnternehmenForDisplay);
		unternehmenGrid.setDataProvider(ldpEvent);
		
		Event vorausgewaehltesEvent = comboBox.getValue();
		if (vorausgewaehltesEvent != null) { //Vereinfachung moeglich, Unternehmen sind ja jetzt bereits in Event und muessen nicht mehr alle geladen werden
			Event selectedEvent = iShowEventService.showEvent(vorausgewaehltesEvent.getEvent_id());
			List<Unternehmen> listofUnternehmen = iShowUnternehmenService.showUnternehmen(); // alle Unternehmen aus DB holen
			// jetzt iterieren wir durch die Teilnehmer des Events und adden diese in unser Grid
			for (Integer unternehmenDesEvents : selectedEvent.getTeilnehmendeUnternehmen()) { //nur die teilnehmenden Unternehmen anzeigen
				for (Unternehmen aUnternehmen : listofUnternehmen) {
					if (aUnternehmen.getUnternehmen_id() == unternehmenDesEvents) {
						listOfUnternehmenForDisplay.add(aUnternehmen);
					}
				}
			}
	
		} else {
			System.out.println("Kein Event vorhanden");
		}
		
		
		comboBox.addValueChangeListener(event -> {
			Event aEvent = comboBox.getValue();
			
			listOfUnternehmenForDisplay.clear();
			
			if (aEvent != null) {
				Event selectedEvent = iShowEventService.showEvent(aEvent.getEvent_id());
				List<Unternehmen> listofUnternehmen = iShowUnternehmenService.showUnternehmen(); // alle Unternehmen aus DB holen
				// jetzt iterieren wir durch die Teilnehmer des Events und adden diese in unser Grid
				for (Integer unternehmenDesEvents : selectedEvent.getTeilnehmendeUnternehmen()) { //nur die teilnehmenden Unternehmen anzeigen
					for (Unternehmen aUnternehmen : listofUnternehmen) {
						if (aUnternehmen.getUnternehmen_id() == unternehmenDesEvents) {
							listOfUnternehmenForDisplay.add(aUnternehmen);
						}
					}
				}
				unternehmenGrid.getDataProvider().refreshAll();	//nach dem Aendern des ComboBox-Wertes wird die Tabelle aktualisiert
			} else {
				System.out.println("Kein Event vorhanden");
			}
		});

		unternehmenGrid.removeColumnByKey("unternehmen_id"); // Feld nicht in Tabelle mit anzeigen
		unternehmenGrid.setColumns("unternehmensname", "ansprechpartner", "kontaktmail"); // Spaltenordnung festlegen
		unternehmenGrid.setSelectionMode(SelectionMode.MULTI); // es koennen mehrere Events ausgewaehlt sein
		selectionModelUnternehmen = (GridMultiSelectionModel<Unternehmen>) unternehmenGrid.getSelectionModel();

		votingSendenButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			for (Unternehmen e : selectionModelUnternehmen.getSelectedItems()) // markierte Events durchgehen
			{
				Studierender einStudierender = iShowStudierendeService.showStudierenden(userDetails.getEntityRefId());

				Set<Integer> studentKontaktwuenscheList = new HashSet<>();
				for (Unternehmen einUnternehmen : selectionModelUnternehmen.getSelectedItems()) {
					studentKontaktwuenscheList.add(einUnternehmen.getUnternehmen_id());
				}
				einStudierender.setStudentKontaktwuensche(studentKontaktwuenscheList);
				iStudierenderService.changeStudierenden(einStudierender);

				notificationVotingSuccess.open();
//				

			}
			votingSendenButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});

		logoutButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
			//getUI().get().getSession().close(); // Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});

		add(comboBox);
		add(unternehmenGrid); // Hinzufuegen der Elemente zum VerticalLayout
		add(votingSendenButton);
		add(logoutButton);
	}
	
	// @PostConstruct //Ausfuehrung nach Konstruktoraufruf
	// public void init()
	// {
	//
	// }
}
