package de.swprojekt.speeddating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.swprojekt.speeddating.model.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {	//JpaRepository-Interface beinhaltet wesentliche Methoden fuer Operationen zum Einfuegen/Loeschen/Aendern von Entities

	@Query("SELECT r FROM Role r WHERE r.role=:role")	//zusaetzliche Methode zu geerbten hinzufuegen
	Role findByRolename(@Param("role") String role);	//findet via OQL Rolle zu angegebenem Namen

}
