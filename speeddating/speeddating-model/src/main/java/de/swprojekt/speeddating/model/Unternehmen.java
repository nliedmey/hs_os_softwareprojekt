package de.swprojekt.speeddating.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Unternehmen {

	@Id // PrimaryKey
	@GeneratedValue(strategy = GenerationType.AUTO) // automatische Generierung und hochzaehlen
	private int unternehmen_id;
	private String unternehmensname;
	private String ansprechpartner;
	private String kontaktmail;

	// Konstruktor
	public Unternehmen() {

	}

	public Unternehmen(int unternehmen_id, String unternehmensname, String ansprechpartner, String kontaktmail) {
		super();
		this.unternehmen_id = unternehmen_id;
		this.unternehmensname = unternehmensname;
		this.ansprechpartner = ansprechpartner;
		this.kontaktmail = kontaktmail;
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

}
