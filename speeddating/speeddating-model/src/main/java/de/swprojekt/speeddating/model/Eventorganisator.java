package de.swprojekt.speeddating.model;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

/*
 * Klasse wird als Entity definiert
 */
@Entity // Entity wird in DB gespeichert (Tabellenname Standard: Klassenname)
public class Eventorganisator {

	@Id // PrimaryKey
	@GeneratedValue(strategy = GenerationType.AUTO) // automatische Generierung und hochzaehlen
	private int eventorganisator_id;
	private String vorname;
	private String nachname;
	private String fachbereich;
	private String telefonnr;
	private String email;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "eventorganisator_event", joinColumns = @JoinColumn(name = "eventorganisator_id"))
	@Column(name = "event_id")
	private Set<Integer> verwaltet_events; // CollectionTable weil Integers und keine Entities
	@Transient
	private int anzahlVerwalteteEvents;


	public Eventorganisator() {
		// TODO Auto-generated constructor stub
	}


	public Eventorganisator(String vorname, String nachname, String fachbereich, String telefonnr, String email,
			Set<Integer> verwaltet_events) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.fachbereich = fachbereich;
		this.telefonnr = telefonnr;
		this.email = email;
		this.verwaltet_events = verwaltet_events;
	}

	public int getEventorganisator_id() {
		return eventorganisator_id;
	}


	public void setEventorganisator_id(int eventorganisator_id) {
		this.eventorganisator_id = eventorganisator_id;
	}


	public String getVorname() {
		return vorname;
	}


	public void setVorname(String vorname) {
		this.vorname = vorname;
	}


	public String getNachname() {
		return nachname;
	}


	public void setNachname(String nachname) {
		this.nachname = nachname;
	}


	public String getFachbereich() {
		return fachbereich;
	}


	public void setFachbereich(String fachbereich) {
		this.fachbereich = fachbereich;
	}


	public String getTelefonnr() {
		return telefonnr;
	}


	public void setTelefonnr(String telefonnr) {
		this.telefonnr = telefonnr;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Set<Integer> getVerwaltet_events() {
		return verwaltet_events;
	}


	public void setVerwaltet_events(Set<Integer> verwaltet_events) {
		this.verwaltet_events = verwaltet_events;
	}


	public int getAnzahlVerwalteteEvents() {
		return anzahlVerwalteteEvents;
	}


	public void setAnzahlVerwalteteEvents(int anzahlVerwalteteEvents) {
		this.anzahlVerwalteteEvents = anzahlVerwalteteEvents;
	}

	
}
