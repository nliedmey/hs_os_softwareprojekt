package de.swprojekt.speeddating.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Klasse wird als Entity definiert
 */
@Entity // Entity wird in DB gespeichert (Tabellenname Standard: Klassenname)
public class Studierender {

	@Id // PrimaryKey
	@GeneratedValue(strategy = GenerationType.AUTO) // automatische Generierung und hochzaehlen
	private int matrikelnummer;
	private String vorname;
	private String nachname;
	private String strasse;
	private int hausnummer;
	private String plz;
	private String ort;
	private String telefonnr;
	private String email;


	public Studierender() {
		// TODO Auto-generated constructor stub
	}

	public Studierender(int matrikelnummer, String vorname, String nachname, String strasse, int hausnummer, String plz,
			String ort, String telefonnr, String email) {
		super();
		this.matrikelnummer = matrikelnummer;
		this.vorname = vorname;
		this.nachname = nachname;
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.plz = plz;
		this.ort = ort;
		this.telefonnr = telefonnr;
		this.email = email;
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

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public int getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(int hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
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



	public int getMatrikelnummer() {
		return matrikelnummer;
	}



	public void setMatrikelnummer(int matrikelnummer) {
		this.matrikelnummer = matrikelnummer;
	}

}
