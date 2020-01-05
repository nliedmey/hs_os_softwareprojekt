package de.swprojekt.speeddating.ui.stud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
/*
 * View fuer die Anzeige vorhandener Studierender
 */

@Route("ui/studs")
@Secured("ROLE_EVENTORGANISATOR")
public class StudView extends VerticalLayout { // VerticalLayout fuehrt zu Anordnung von Elementen untereinander statt
												// nebeneinander (HorizontalLayout)

	@Autowired // Konstruktor-basierte Injection, Parameter wird autowired (hier: Interface)
	public StudView(IShowStudierendeService iShowStudierendeService) {

		Grid<Studierender> studierenderGrid; // Tabelle mit Studierenden
		Button logoutButton = new Button("Logout");
		Button zurueckButton = new Button("Zurueck");
		zurueckButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

		studierenderGrid = new Grid<>(Studierender.class); // Tabelle initialisieren
		ListDataProvider<Studierender> ldpStudent = DataProvider
				.ofCollection(iShowStudierendeService.showStudierende()); // Dataprovider erstellen und Quelle fuer
																			// Studierende (via Service aus DB)
																			// festlegen
		studierenderGrid.setDataProvider(ldpStudent); // erstellten Dataprovider als Datenquelle fuer Tabelle festlegen

		studierenderGrid.removeColumnByKey("student_id"); // studId nicht in Tabelle mit anzeigen
		studierenderGrid.setColumns("matrikelnummer","vorname", "nachname", "strasse", "hausnummer", "plz", "ort", "telefonnr", "email" ); // Spaltenordnung festlegen
	
		
		logoutButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			SecurityContextHolder.clearContext(); // Spring-Security-Session leeren
			// getUI().get().getSession().close(); //Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui -> ui.navigate("login")); // zurueck auf andere Seite
		});

		zurueckButton.addClickListener(event -> { // Bei Buttonklick werden folgende Aktionen ausgefuehrt
			zurueckButton.getUI().ifPresent(ui -> ui.navigate("ui/eventorganisator/menue")); // zurueck auf andere Seite
		});

		add(studierenderGrid); // Hinzufuegen der Elemente zum VerticalLayout
		add(new HorizontalLayout(zurueckButton, logoutButton));
	}
	// @PostConstruct //Ausfuehrung nach Konstruktoraufruf
	// public void init()
	// {
	//
	// }
}
