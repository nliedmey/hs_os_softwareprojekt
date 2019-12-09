package de.swprojekt.speeddating.model;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
/*
 * Entity fuer Nutzer der Webanwendung
 * Implementiert UserDetails-Interface von SpringSecurity
 * Ermoeglicht die spaetere Umwandlung von User-Objekt in UserDetails-Objekt, welches von SpringSecurity u.a. zur Autorisierung verwendet wird
 */
@Entity
public class User implements UserDetails{

	@Id
	@GeneratedValue
	private int user_id;

	private String username;

	private String password;	//passwort wird im RegisterUserServiceImpl verschluesselt (sichere BCrypt-Hashing-Funktion)

	//CascadeType nicht all inkl. Persist, da dann bei Setten von Rolle diese automatisch auch erneut gespeichert wuerde (ist aber bereits vorhanden)
	@ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.MERGE}, fetch = FetchType.EAGER) // ein user kann mehrere Rollen haben, einer Rolle koennen mehrere User zugeordnet sein
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))	//mappingtabelle user_role erstellen
	private Set<Role> roles;
	
	private int entity_id_ref; //referenziert auf StudentenID bzw. UnternehmenID bzw. EventorganisatorID
	
	public User()
	{
		
		//wird von JPA genutzt
	}
	
	public User(Set<Role> roles)
	{
		this.roles=roles;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		for(Role r:this.roles)//Schleife zur Anzeige von Roles, welche der User innehat
		{
			System.out.println("Role: "+r.getRole());
		}
		return this.roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole())).collect(Collectors.toList());	//Roles des Users werden als List zurueckgegeben
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getEntity_id_ref() {
		return entity_id_ref;
	}

	public void setEntity_id_ref(int entity_id_ref) {
		this.entity_id_ref = entity_id_ref;
	}

	@Override
	public boolean isAccountNonExpired() {	//derzeit nicht genutzt (immer true)
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {	//derzeit nicht genutzt (immer true)
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {	//derzeit nicht genutzt (immer true)
		return true;
	}

	@Override
	public boolean isEnabled() {	//derzeit nicht genutzt (immer true)
		return true;
	}


}
