package de.swprojekt.speeddating.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/*
 * Rollen werden den Usern zugewiesen (z.B. ADMIN)
 */
@Entity
public class Role {
	@Id
	@GeneratedValue
	private int role_id;
	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
