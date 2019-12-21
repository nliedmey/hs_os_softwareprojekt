package de.swprojekt.speeddating.ui.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Role;
import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IRoleRepository;
import de.swprojekt.speeddating.repository.IUserRepository;
import de.swprojekt.speeddating.ui.MainLayout;

/*
 * Temporaere Loesung zum Erstellen von ADMIN!!!SICHERHEITSRISIKO!!
 */
@Route(value = "admininit", layout = MainLayout.class) // Abgeleitet von Root-Layout MainLayout
public class AdminInit extends VerticalLayout {



	@Autowired
	public AdminInit(IRoleRepository iRoleRepository, IUserRepository iUserRepository, BCryptPasswordEncoder passwordEncoder) {

		Button buttonAdminerstellen=new Button("ADMIN ERSTELLEN");
		Label labelPWUsername=new Label("Name: admin, pw: admin, kann Menue ueber /ui/admin/menue erreichen und von dort eventorgas etc. erstellen");
		
		User einUser=new User();
		einUser.setPassword(passwordEncoder.encode("admin"));
		einUser.setUsername("admin");
		Set<Role> roles=new HashSet<Role>();
		roles.add(iRoleRepository.findByRolename("ADMIN"));
		einUser.setRoles(roles);
	
		
		buttonAdminerstellen.addClickListener(event -> {
			iUserRepository.save(einUser);
			System.out.println("ADMIN ERSTELLT!");
			
		});
		add(labelPWUsername);
		add(buttonAdminerstellen);
		

	}

}
