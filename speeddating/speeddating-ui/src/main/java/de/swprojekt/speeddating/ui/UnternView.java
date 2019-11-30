package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
/*
 * View fuer die Anzeige vorhandener Studierender
 */
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;

@Route("ui/untern")	//Erreichbar ueber Adresse: http://localhost:8080/speeddating-web-7.0-SNAPSHOT/ui/studs
@Secured("ROLE_ADMIN")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class UnternView extends VerticalLayout {	//VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt nebeneinander (HorizontalLayout)

	@Autowired	//Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public UnternView(IShowUnternehmenService iShowUnternehmenService) {
	
		Grid<Unternehmen> unternehmenGrid;	//Tabelle mit Studierenden
		Button logoutButton=new Button("Logout");
		
		unternehmenGrid = new Grid<>(Unternehmen.class);	//Tabelle initialisieren
		ListDataProvider<Unternehmen> ldpUnternehmen = DataProvider
				.ofCollection(iShowUnternehmenService.showUnternehmen());	//Dataprovider erstellen und Quelle fuer Studierende (via Service aus DB) festlegen 
		unternehmenGrid.setDataProvider(ldpUnternehmen);	//erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		//studierenderGrid.removeColumnByKey("studId");	//studId nicht in Tabelle mit anzeigen
		unternehmenGrid.setColumns("unternehmensname", "ansprechpartner");	//Spaltenordnung festlegen
		logoutButton.addClickListener(event -> {	//Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});


		add(unternehmenGrid);	//Hinzufuegen der Elemente zum VerticalLayout
		add(logoutButton);
	}
	//@PostConstruct	//Ausfuehrung nach Konstruktoraufruf
	//public void init()
	//{
	//	
	//}
}
