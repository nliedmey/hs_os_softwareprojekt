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

import org.hibernate.hql.internal.ast.util.ASTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import antlr.collections.AST;
import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.repository.IEventRepository;
import de.swprojekt.speeddating.service.addstudierender.IStudierenderService;
import de.swprojekt.speeddating.service.deleteevent.IDeleteEventService;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;
import de.swprojekt.speeddating.service.unternehmen.IUnternehmenService;
/*
 * View fuer die Anzeige vorhandener Events
 */

@Route("ui/eventVotingView_Stud") // Erreichbar ueber Adresse:
									// http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/events
//@Secured("ROLE_ADMIN")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class EventVotingView_Stud extends VerticalLayout { // VerticalLayout fuehrt zu Anordnung von Elementen
															// untereinander statt nebeneinander (HorizontalLayout)
	int lv_student_id = 0;

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
		List<Event> listOfEvents = iShowEventService.showEvents();
		comboBox.setItems(listOfEvents);

		ComboBox<Studierender> comboBox2 = new ComboBox<>();
		comboBox2.setLabel("Student auswaehlen");
		comboBox2.setItemLabelGenerator(Studierender::getStringFullNameOfStudent);
		List<Studierender> listOfStudierende = iShowStudierendeService.showStudierende();
		comboBox2.setItems(listOfStudierende);

		Notification notificationVotingSuccess = new Notification();
		notificationVotingSuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelVotingsuccess = new Label("Voting erfolgreich abgegeben! ");
		notificationVotingSuccess.add(labelVotingsuccess);

		comboBox2.addValueChangeListener(event -> {
			Studierender aStudent = comboBox2.getValue();
			if (aStudent != null) {
				lv_student_id = aStudent.getStudent_id();
			}
		});

		comboBox.addValueChangeListener(event -> {
			Event aEvent = comboBox.getValue();

			// Datagrid initialisieren
			listOfUnternehmenForDisplay.clear();
			ListDataProvider<Unternehmen> ldpEvent = DataProvider.ofCollection(listOfUnternehmenForDisplay);
			unternehmenGrid.setDataProvider(ldpEvent);

			if (aEvent != null) {
				Event selectedEvent = iShowEventService.showEvent(aEvent.getEvent_id());
				List<Unternehmen> listofUnternehmen = iShowUnternehmenService.showUnternehmen(); // alle Unternehmen aus DB
																									// holen
				// jetzt iterieren wir durch die Teilnehmer des Events und adden diese in unser
				// Grid
				for (Integer unternehmenDesEvents : selectedEvent.getTeilnehmendeUnternehmen()) { //nur die teilnehmenden Unternehmen anzeigen
					for (Unternehmen aUnternehmen : listofUnternehmen) {
						if (aUnternehmen.getUnternehmen_id() == unternehmenDesEvents) {
							listOfUnternehmenForDisplay.add(aUnternehmen);
						}
					}
				}
				// die Teilnehmenden Unternehmen des Events fuegen wir jetzt unserem Grid hinzu
				ldpEvent = DataProvider.ofCollection(listOfUnternehmenForDisplay);
				unternehmenGrid.setDataProvider(ldpEvent);
			} else {
				// message.setText("No song is selected");
			}
		});

		unternehmenGrid.removeColumnByKey("unternehmen_id"); // Feld nicht in Tabelle mit anzeigen
		unternehmenGrid.setColumns("unternehmensname", "ansprechpartner", "kontaktmail"); // Spaltenordnung festlegen
		unternehmenGrid.setSelectionMode(SelectionMode.MULTI); // es koennen mehrere Events ausgewaehlt sein
		selectionModelUnternehmen = (GridMultiSelectionModel<Unternehmen>) unternehmenGrid.getSelectionModel();

		votingSendenButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			for (Unternehmen e : selectionModelUnternehmen.getSelectedItems()) // markierte Events durchgehen
			{

				// TODO: Eigentlich ist der Benutzer ein Student, also muessten wir die
				// StduentID irgendwie ueber den
				// Benutzer bekommen, da wir das noch nicht realisiert haben, machen wir uns das
				// hier erstmal einfach
				// und setzen/waehlen den Studenten per ComboBox aus
				Studierender einStudierender = iShowStudierendeService.showStudierenden(lv_student_id);

				Set<Integer> studentKontaktwuenscheList = new HashSet<>();
				for (Unternehmen einUnternehmen : selectionModelUnternehmen.getSelectedItems()) {
					studentKontaktwuenscheList.add(einUnternehmen.getUnternehmen_id());
				}
				einStudierender.setStudentKontaktwuensche(studentKontaktwuenscheList);
				iStudierenderService.changeStudierenden(einStudierender);

				notificationVotingSuccess.open();
//				SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//				getUI().get().getSession().close();		//Vaadin Session leeren
				votingSendenButton.getUI().ifPresent(ui -> ui.navigate("maincontent")); // zurueck auf andere Seite

			}
		});

		logoutButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
			getUI().get().getSession().close(); // Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("maincontent")); // zurueck auf andere Seite
		});

		add(comboBox2,comboBox);
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
