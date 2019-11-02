package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.grid.Grid;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;

@Route("ui/studs")
public class StudView extends VerticalLayout {

	@Autowired
	public StudView(IShowStudierendeService iShowStudierendeService) {
		System.out.println("X" + iShowStudierendeService);

		Grid<Studierender> studierenderGrid;

		studierenderGrid = new Grid<>(Studierender.class);
		ListDataProvider<Studierender> ldpStudent = DataProvider
				.ofCollection(iShowStudierendeService.showStudierende());
		studierenderGrid.setDataProvider(ldpStudent);

		studierenderGrid.removeColumnByKey("studId");
		studierenderGrid.setColumns("vorname", "nachname", "hauptfach");

		add(studierenderGrid);
	}
	//@PostConstruct
	//public void init()
	//{
	//	
	//}
}
