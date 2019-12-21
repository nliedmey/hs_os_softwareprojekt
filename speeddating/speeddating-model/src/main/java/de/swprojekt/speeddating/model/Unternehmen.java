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

@Entity
public class Unternehmen {

	@Id // PrimaryKey
	@GeneratedValue(strategy = GenerationType.AUTO) // automatische Generierung und hochzaehlen
	private int unternehmen_id;
	private String unternehmensname;
	private String ansprechpartner;
	private String kontaktmail;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "unternehmen_wants_studenten", joinColumns = @JoinColumn(name = "unternehmen_id"))
	@Column(name = "student_id")
	private Set<Integer> unternehmenKontaktwuensche; // CollectionTable weil Integers und keine Entities

	// Konstruktor
	public Unternehmen() {

	}

	public Unternehmen(int unternehmen_id, String unternehmensname, String ansprechpartner, String kontaktmail,
			Set<Integer> unternehmenKontaktwuensche) {
		super();
		this.unternehmen_id = unternehmen_id;
		this.unternehmensname = unternehmensname;
		this.ansprechpartner = ansprechpartner;
		this.kontaktmail = kontaktmail;
		this.unternehmenKontaktwuensche = unternehmenKontaktwuensche;
	}

	public int getUnternehmen_id() {
		return unternehmen_id;
	}

	public void setUnternehmen_id(int unternehmen_id) {
		this.unternehmen_id = unternehmen_id;
	}

	public String getUnternehmensname() {
		return unternehmensname;
	}

	public void setUnternehmensname(String unternehmensname) {
		this.unternehmensname = unternehmensname;
	}

	public String getAnsprechpartner() {
		return ansprechpartner;
	}

	public void setAnsprechpartner(String ansprechpartner) {
		this.ansprechpartner = ansprechpartner;
	}

	public String getKontaktmail() {
		return kontaktmail;
	}

	public void setKontaktmail(String kontaktmail) {
		this.kontaktmail = kontaktmail;
	}

	public Set<Integer> getUnternehmenKontaktwuensche() {
		return unternehmenKontaktwuensche;
	}

	public void setUnternehmenKontaktwuensche(Set<Integer> unternehmenKontaktwuensche) {
		this.unternehmenKontaktwuensche = unternehmenKontaktwuensche;
	}

	public String getNameAndContact() {
		return this.unternehmensname + ", " + this.ansprechpartner + ", " + this.kontaktmail;
	}
}
