package de.swprojekt.speeddating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.swprojekt.speeddating.model.Role;
import de.swprojekt.speeddating.model.User;

public interface IRoleRepository extends JpaRepository<Role, Integer> {
	
//	@Query("SELECT u FROM User u WHERE u.username=:username")
//	User findByUsername(@Param("username")String username);

}
