package de.swprojekt.speeddating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.swprojekt.speeddating.model.Event;
/*
 * Repository zur Eventverwaltung
 */
@Repository
public interface IEventRepository extends JpaRepository<Event, Integer> {

}


