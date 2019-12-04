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

@Route("ui/eventVotingView_Untern")	//Erreichbar ueber Adresse: http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/events
//@Secured("ROLE_ADMIN")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class EventVotingView_Untern extends VerticalLayout {	//VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt nebeneinander (HorizontalLayout)

	int lv_unternehmen_id = 0;
	
	@Autowired	//Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public EventVotingView_Untern(IShowEventService iShowEventService, IShowUnternehmenService iShowUnternehmenService,
			IUnternehmenService iUnternehmenService, IShowStudierendeService iShowStudierenderService) {

		Grid<Studierender> studierenderGrid;	//Tabelle mit Events
		studierenderGrid = new Grid<>(Studierender.class);	//Tabelle initialisieren
		GridMultiSelectionModel<Studierender> selectionModelStudierender;
		List<Studierender> listOfStudierendeForDisplay = new ArrayList<Studierender>();
		Button votingSendenButton=new Button("Kontaktwuensche absenden");		
		Button logoutButton=new Button("Logout");		
		
		// Erzeugen der Combo Box
		ComboBox<Event> comboBox = new ComboBox<>();
		comboBox.setLabel("Event auswaehlen");
		comboBox.setItemLabelGenerator(Event:: getBezeichnung);
		List<Event> listOfEvents = iShowEventService.showEvents();
		comboBox.setItems(listOfEvents);
		
		
		ComboBox<Unternehmen> comboBox2 = new ComboBox<>();
		comboBox2.setLabel("Unternehmen auswaehlen");
		comboBox2.setItemLabelGenerator(Unternehmen::getUnternehmensname);
		List<Unternehmen> listOfUnternehmen = iShowUnternehmenService.showUnternehmen();
		comboBox2.setItems(listOfUnternehmen);
				
		Notification notificationVotingSuccess = new Notification();
		notificationVotingSuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelVotingsuccess = new Label("Voting erfolgreich abgegeben! ");
		notificationVotingSuccess.add(labelVotingsuccess);
		
		comboBox2.addValueChangeListener(event -> {
			Unternehmen aUnternehmen = comboBox2.getValue();
			if (aUnternehmen != null) {
				lv_unternehmen_id = aUnternehmen.getUnternehmen_id();
			}
		});
		
		
		comboBox.addValueChangeListener(event -> {
			Event aEvent = comboBox.getValue();		
			
			//Datagrid initialisieren
			listOfStudierendeForDisplay.clear();
			ListDataProvider<Studierender> ldpEvent = DataProvider
					.ofCollection(listOfStudierendeForDisplay);			
			studierenderGrid.setDataProvider(ldpEvent);
			
			if (aEvent != null) {	
		        Event selectedEvent = iShowEventService.showEvent(aEvent.getEvent_id());
		        				
				List<Studierender> listofStudierende = iShowStudierenderService.showStudierende(); //alle Studierenden holen					
				
				//jetzt iterieren wir durch die Teilnehmer des Events und adden diese in unser Grid
				for( Integer studierendeDesEvents : selectedEvent.getTeilnehmendeStudierende()) { 			
					for( Studierender aStudierender: listofStudierende) {						
						if (aStudierender.getStudent_id() == studierendeDesEvents) {							
							listOfStudierendeForDisplay.add(aStudierender);
						}						
					}					
				}
				
				// die Teilnehmenden Unternehmen des Events fuegen wir jetzt unserem Grid hinzu
				 ldpEvent = DataProvider.ofCollection(listOfStudierendeForDisplay);			
				studierenderGrid.setDataProvider(ldpEvent);
			} else {
				// message.setText("No song is selected");
			}

		});
					
		studierenderGrid.removeColumnByKey("student_id");	//Feld nicht in Tabelle mit anzeigen
		studierenderGrid.setColumns("vorname", "nachname", "email", "ort", "plz", "strasse");	//Spaltenordnung festlegen
		studierenderGrid.setSelectionMode(SelectionMode.MULTI);	//es koennen mehrere Events ausgewaehlt sein
		selectionModelStudierender = (GridMultiSelectionModel<Studierender>) studierenderGrid.getSelectionModel();
		
			
		votingSendenButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			for(Studierender e:selectionModelStudierender.getSelectedItems())	//markierte Events durchgehen
			{
				
				// TODO: Eigentlich ist der Benutzer ein Unternehmen, also muessten wir die
				// UnternehmenID irgendwie ueber den
				// Benutzer bekommen, da wir das noch nicht realisiert haben, machen wir uns das
				// hier erstmal einfach
				// und setzen/waehlen das Unternehmen per ComboBox aus
				Unternehmen einUnternehmen = iShowUnternehmenService.showEinUnternehmen(lv_unternehmen_id);

				Set<Integer> unternehmenKontaktwuenscheList = new HashSet<>();
				for (Studierender einStudierender : selectionModelStudierender.getSelectedItems()) {
					unternehmenKontaktwuenscheList.add(einStudierender.getStudent_id());
				}
				einUnternehmen.setUnternehmenKontaktwuensche(unternehmenKontaktwuenscheList);
				iUnternehmenService.changeUnternehmen(einUnternehmen);

				notificationVotingSuccess.open();
//				SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
//				getUI().get().getSession().close();		//Vaadin Session leeren
				votingSendenButton.getUI().ifPresent(ui -> ui.navigate("maincontent")); // zurueck auf andere Seite

				
				
			}
		});
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});

		add(comboBox2, comboBox);
		add(studierenderGrid);	//Hinzufuegen der Elemente zum VerticalLayout
		add(votingSendenButton);
		add(logoutButton);
	}
	//@PostConstruct	//Ausfuehrung nach Konstruktoraufruf
	//public void init()
	//{
	//	
	//}
}
