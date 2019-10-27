package de.swprojekt.speeddating.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Studierender {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int studId;
	private String vorname;
	private String nachname;
	private String hauptfach;
	
	public Studierender() {
		// TODO Auto-generated constructor stub
	}
	
	public Studierender(int studId, String vorname, String nachname, String hauptfach) {
		super();
		this.studId = studId;
		this.vorname = vorname;
		this.nachname = nachname;
		this.hauptfach = hauptfach;
	}

	public int getStudId() {
		return studId;
	}

	public void setStudId(int studId) {
		this.studId = studId;
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

	public String getHauptfach() {
		return hauptfach;
	}

	public void setHauptfach(String hauptfach) {
		this.hauptfach = hauptfach;
	}
}