package de.swprojekt.speeddating.ui;


import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
/*
 * Fehlerseite, wird angezeigt bei fehlenden Rechten fuer Seitenaufruf
 */
@Route("zugangVerwehrt")
public class ZugangVerwehrt extends VerticalLayout {

	private Label zugangVerwehrtLabel;
	

	public ZugangVerwehrt() {
		zugangVerwehrtLabel=new Label("ZUGANG verwehrt! Keine Rechte fuer Seitenaufruf!");
		add(zugangVerwehrtLabel);
	}

}
