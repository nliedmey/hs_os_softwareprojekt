package de.swprojekt.speeddating.repository;

import java.util.List;

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
	
	@Query(
			value = "SELECT u.user_id" +  //"SELECT u.user_id, u.username, e.event_id, unt.unternehmen_id, unt.unternehmensname, unt.ansprechpartner" + 
					" FROM unternehmen AS unt" + 
					" INNER JOIN event_unternehmen AS eu ON unt.unternehmen_id=eu.unternehmen_id" + 
					" INNER JOIN event AS e ON e.event_id=eu.event_id" + 
					" INNER JOIN user AS u ON u.entity_id_ref=eu.unternehmen_id"+
					" WHERE e.event_id=?1", 
			  nativeQuery = true)
	List<Integer> findUnternehmenUserByEventId(int event_id); //Alle User-IDs fuer die Unternehmen eines Events holen
	
	
	@Query(
			value = "SELECT u.user_id" + //"SELECT u.user_id, u.username, e.event_id, s.student_id, s.matrikelnummer, s.vorname, s.nachname" + 
					" FROM studierender AS s" + 
					" INNER JOIN event_studierender AS es ON s.student_id=es.student_id" + 
					" INNER JOIN event AS e ON e.event_id=es.event_id" + 
					" INNER JOIN user AS u ON u.entity_id_ref=es.student_id"+
					" WHERE e.event_id=?1", 
			  nativeQuery = true)
	List<Integer> findStudierendeUserByEventId(int event_id); //Alle User-IDs fuer die Studierenden eines Events holen
	
	@Query(
			value = "SELECT DISTINCT u.user_id" +
					" FROM eventorganisator AS eo" + 
					" INNER JOIN eventorganisator_event AS eoe ON eo.eventorganisator_id=eoe.eventorganisator_id" + 
					" INNER JOIN user AS u ON u.entity_id_ref=eoe.eventorganisator_id", 
			  nativeQuery = true)
	List<Integer> findEventorganisatorenUser(); //Alle User-IDs fuer die Eventorganisatoren holen

}
