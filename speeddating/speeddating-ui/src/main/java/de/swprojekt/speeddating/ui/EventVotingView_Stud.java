package de.swprojekt.speeddating.ui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.Grid.SelectionMode;

import java.util.List;

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
import de.swprojekt.speeddating.service.deleteevent.IDeleteEventService;
import de.swprojekt.speeddating.service.showevent.IShowEventService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;
import de.swprojekt.speeddating.service.unternehmen.IUnternehmenService;
/*
 * View fuer die Anzeige vorhandener Events
 */  

@Route("ui/eventVotingView_Stud")	//Erreichbar ueber Adresse: http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/events
//@Secured("ROLE_ADMIN")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class EventVotingView_Stud extends VerticalLayout {	//VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt nebeneinander (HorizontalLayout)

	@Autowired	//Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public EventVotingView_Stud(IShowEventService iShowEventService, IShowUnternehmenService iShowUnternehmenService) {
	
		Grid<Unternehmen> unternehmenGrid;	//Tabelle mit Events
		unternehmenGrid = new Grid<>(Unternehmen.class);	//Tabelle initialisieren
		GridMultiSelectionModel<Unternehmen> selectionModelUnternehmen;
		Button votingSendenButton=new Button("Kontaktwuensche absenden");		
		Button logoutButton=new Button("Logout");
		
		
		// Erzeugen der Combo Box
		ComboBox<Event> comboBox = new ComboBox<>();
		comboBox.setLabel("Event auswaehlen");
		comboBox.setItemLabelGenerator(Event::getBezeichnung);
		List<Event> listOfEvents = iShowEventService.showEvents();
		List<Unternehmen> listOfUnternehmenForDisplay = null;

		comboBox.setItems(listOfEvents);
		comboBox.addValueChangeListener(event -> {
			Event aEvent = comboBox.getValue();
			
			if (aEvent != null) {				
				List<Unternehmen> listofUnternehmen = iShowUnternehmenService.showUnternehmen(); //alle Unternehmen holen					
				for( Integer unternehmenDesEvents : aEvent.getTeilnehmendeUnternehmen()) { 
					for( Unternehmen aUnternehmen: listofUnternehmen) {						
						if (aUnternehmen.getUnternehmen_id() == unternehmenDesEvents) {							
							listOfUnternehmenForDisplay.add(aUnternehmen);
						}						
					}					
				}
				
				ListDataProvider<Unternehmen> ldpEvent = DataProvider
						.ofCollection(listOfUnternehmenForDisplay);			
				unternehmenGrid.setDataProvider(ldpEvent);	//erstellten Dataprovider als Datenquelle fuer Tabelle festlegen
			} else {
				// message.setText("No song is selected");
			}

		});
					
		unternehmenGrid.removeColumnByKey("unternehmen_id");	//event_id nicht in Tabelle mit anzeigen
		unternehmenGrid.setColumns("unternehmensname", "ansprechpartner", "kontaktmail");	//Spaltenordnung festlegen
		unternehmenGrid.setSelectionMode(SelectionMode.MULTI);	//es koennen mehrere Events ausgewaehlt sein
		selectionModelUnternehmen = (GridMultiSelectionModel<Unternehmen>) unternehmenGrid.getSelectionModel();
		
//		VotingSendenButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
//			for(Event e:selectionModelEvent.getSelectedItems())	//markierte Events durchgehen
//			{
//				iDeleteEventService.loescheEvent(e);
//			}
//		});
		
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});

		add(comboBox);
		add(unternehmenGrid);	//Hinzufuegen der Elemente zum VerticalLayout
		add(votingSendenButton);
		add(logoutButton);
	}
	//@PostConstruct	//Ausfuehrung nach Konstruktoraufruf
	//public void init()
	//{
	//	
	//}
}
