package de.swprojekt.speeddating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.swprojekt.speeddating.model.User;
/*
 * Repository zur Userverwaltung
 * Erweitert Spring-Interface JpaRepository, welches Methoden wie find/save definiert und zur Laufzeit die Implementierung (org.springframework.data.jpa.repository.support.SimpleJpaRepository.class) einbindet
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.username=:username")
	User findByUsername(@Param("username") String username);

}
