package de.swprojekt.speeddating.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import de.swprojekt.speeddating.model.Event;

/*
 * Repository zur Eventverwaltung
 */
@Repository
public interface IEventRepository extends JpaRepository<Event, Integer> {
//	@Query("SELECT e FROM Event e WHERE e.teilnehmendeStudierende=:userId")	//zusaetzliche Methode zu geerbten hinzufuegen
//	@Query(
//			  value = "select event_id from event_studierender WHERE student_id=?1 UNION select event_id from event_unternehmen WHERE unternehmen_id=?1;", 
//			  nativeQuery = true)
//	List<Event> findByUserId(@Param("userId") int userId);	//findet via OQL Rolle zu angegebenem Namen
	
	@Query(
			  value = "select event_id from event_studierender WHERE student_id=?1 UNION select event_id from event_unternehmen WHERE unternehmen_id=?1 UNION select event_id from eventorganisator_event WHERE eventorganisator_id=?1", 
			  nativeQuery = true)
	List<Integer> findByUserId(int userId);	//findet via OQL Rolle zu angegebenem Namen
	
//	Collection<Event> findByTeilnehmendeStudierendeIn(int userId);
}


