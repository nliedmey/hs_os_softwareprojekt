package de.swprojekt.speeddating.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
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
	private int rundendauerInMinuten;
	private boolean abgeschlossen;
	// private List<Integer> teilnehmendeUnternehmen;
	// private int zustaendigerOrganisator;
	// TODO: hinzuefuegen von Unternehmen und Beziehungen zu diesen
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "event_unternehmen", joinColumns = @JoinColumn(name = "event_id"))
	@Column(name = "unternehmen_id")
	private Set<Integer> teilnehmendeUnternehmen; // CollectionTable weil Integers und keine Entities

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "event_studierender", joinColumns = @JoinColumn(name = "event_id"))
	@Column(name = "student_id")
	private Set<Integer> teilnehmendeStudierende; // CollectionTable weil Integers und keine Entities

	// keine Entities von anderen Klassen in dieser Klasse!
	// @ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.MERGE}, fetch =
	// FetchType.EAGER) // ein event kann mehrere Studenten haben, einem Studenten
	// koennen mehrere Events zugeordnet sein
	// @JoinTable(name = "event_studierender", joinColumns = @JoinColumn(name =
	// "event_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
	// //mappingtabelle event_studierender erstellen
	// private List<Integer> teilnehmendeStudierende;
	
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

	public int getRundendauerInMinuten() {
		return rundendauerInMinuten;
	}

	public void setRundendauerInMinuten(int rundendauerInMinuten) {
		this.rundendauerInMinuten = rundendauerInMinuten;
	}

	public boolean isAbgeschlossen() {
		return abgeschlossen;
	}

	public void setAbgeschlossen(boolean abgeschlossen) {
		this.abgeschlossen = abgeschlossen;
	}

	public Set<Integer> getTeilnehmendeStudierende() {
		return teilnehmendeStudierende;
	}

	public void setTeilnehmendeStudierende(Set<Integer> teilnehmendeStudierende) {
		this.teilnehmendeStudierende = teilnehmendeStudierende;
	}

	public Set<Integer> getTeilnehmendeUnternehmen() {
		return teilnehmendeUnternehmen;
	}

	public void setTeilnehmendeUnternehmen(Set<Integer> teilnehmendeUnternehmen) {
		this.teilnehmendeUnternehmen = teilnehmendeUnternehmen;
	}



}
