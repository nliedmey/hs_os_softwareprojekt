package de.swprojekt.speeddating.model;

public class EventMatching {
	
	
	private String studentenname;
	private String unternehmensname;
	
	public EventMatching(String studentenname, String unternehmensname) {
		super();
		this.studentenname = studentenname;
		this.unternehmensname = unternehmensname;
	}	
	
	
	public String getStudentenname() {
		return studentenname;
	}
	public void setStudentenname(String studentenname) {
		this.studentenname = studentenname;
	}
	public String getUnternehmensname() {
		return unternehmensname;
	}
	public void setUnternehmensname(String unternehmensname) {
		this.unternehmensname = unternehmensname;
	}

	
	
}
