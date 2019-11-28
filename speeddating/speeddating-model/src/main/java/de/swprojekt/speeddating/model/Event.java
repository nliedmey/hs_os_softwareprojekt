package de.swprojekt.speeddating.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Klasse wird als Entity definiert
 */
@Entity // Entity wird in DB gespeichert (Tabellenname Standard: Klassenname)
public class Event {

	@Id // PrimaryKey
	@GeneratedValue(strategy = GenerationType.AUTO) // automatische Generierung und hochzaehlen
	private int event_id;
	private String bezeichnung;
	private Date startzeitpunkt;
	private Date endzeitpunkt;
	private boolean abgeschlossen;
	private List<Studierender> teilnehmendeStudierende;
	//private List<Unternehmen> teilnehmendeUnternehmen;
	//private EventOrganisator zustaendigerOrganisator;

	public Event() {
		// TODO Auto-generated constructor stub
	}

	public Event(String bezeichnung, Date startzeitpunkt, Date endzeitpunkt, boolean abgeschlossen) {
		super();
		this.bezeichnung = bezeichnung;
		this.startzeitpunkt = startzeitpunkt;
		this.endzeitpunkt = endzeitpunkt;
		this.abgeschlossen = abgeschlossen;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public Date getStartzeitpunkt() {
		return startzeitpunkt;
	}

	public void setStartzeitpunkt(Date startzeitpunkt) {
		this.startzeitpunkt = startzeitpunkt;
	}

	public Date getEndzeitpunkt() {
		return endzeitpunkt;
	}

	public void setEndzeitpunkt(Date endzeitpunkt) {
		this.endzeitpunkt = endzeitpunkt;
	}

	public boolean isAbgeschlossen() {
		return abgeschlossen;
	}

	public void setAbgeschlossen(boolean abgeschlossen) {
		this.abgeschlossen = abgeschlossen;
	}

}
