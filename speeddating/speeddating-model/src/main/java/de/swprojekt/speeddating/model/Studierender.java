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

/*
 * Klasse wird als Entity definiert
 */
@Entity // Entity wird in DB gespeichert (Tabellenname Standard: Klassenname)
public class Studierender {

	@Id // PrimaryKey
	@GeneratedValue(strategy = GenerationType.AUTO) // automatische Generierung und hochzaehlen
	private int student_id;
	private int matrikelnummer;
	private String vorname;
	private String nachname;
	private String strasse;
	private int hausnummer;
	private String plz;
	private String ort;
	private String telefonnr;
	private String email;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "student_wants_Unternehmen", joinColumns = @JoinColumn(name = "student_id"))
	@Column(name = "unternehmen_id")
	private Set<Integer> studentKontaktwuensche; // CollectionTable weil Integers und keine Entities



	public Studierender() {
		// TODO Auto-generated constructor stub
	}

	public Studierender(int student_id, int matrikelnummer, String vorname, String nachname, String strasse,
			int hausnummer, String plz, String ort, String telefonnr, String email) {
		super();
		this.student_id = student_id;
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

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	
	public String getStringFullNameOfStudent () {
		return this.vorname + " " + this.nachname;
	}

	public Set<Integer> getStudentKontaktwuensche() {
		return studentKontaktwuensche;
	}

	public void setStudentKontaktwuensche(Set<Integer> studentKontaktwuensche) {
		this.studentKontaktwuensche = studentKontaktwuensche;
	}

}
