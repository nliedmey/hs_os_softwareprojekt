package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.UI;
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
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;

@Route("ui/studs")
@Secured("ROLE_ADMIN")	//nur User mit Rolle ADMIN koennen auf Seite zugreifen, @Secured prueft auch bei RouterLink-Weiterleitungen
//@Secured kann auch an einzelnen Methoden angewendet werden
public class StudView extends VerticalLayout {

	@Autowired
	public StudView(IShowStudierendeService iShowStudierendeService) {
	
		Grid<Studierender> studierenderGrid;
		Button logoutButton=new Button("Logout");
		
		studierenderGrid = new Grid<>(Studierender.class);
		ListDataProvider<Studierender> ldpStudent = DataProvider
				.ofCollection(iShowStudierendeService.showStudierende());
		studierenderGrid.setDataProvider(ldpStudent);

		studierenderGrid.removeColumnByKey("studId");
		studierenderGrid.setColumns("vorname", "nachname", "hauptfach");
		logoutButton.addClickListener(event -> {
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			logoutButton.getUI().ifPresent(ui->ui.navigate("maincontent"));	//zurueck auf andere Seite 
		});


		add(studierenderGrid);
		add(logoutButton);
	}
	//@PostConstruct
	//public void init()
	//{
	//	
	//}
}
