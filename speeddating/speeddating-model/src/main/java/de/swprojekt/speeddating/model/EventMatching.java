package de.swprojekt.speeddating.model;

public class EventMatching {
	
	private int student_id;
	private int unternehmen_id;
	
	public EventMatching(int student_id, int unternehmen_id) {
		super();
		this.student_id = student_id;
		this.unternehmen_id = unternehmen_id;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public int getUnternehmen_id() {
		return unternehmen_id;
	}
	public void setUnternehmen_id(int unternehmen_id) {
		this.unternehmen_id = unternehmen_id;
	}
}
