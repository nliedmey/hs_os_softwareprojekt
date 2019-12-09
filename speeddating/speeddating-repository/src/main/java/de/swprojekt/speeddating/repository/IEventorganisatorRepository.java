package de.swprojekt.speeddating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.swprojekt.speeddating.model.Eventorganisator;
/*
 * Repository zur Eventorganisatorverwaltung
 */
@Repository
public interface IEventorganisatorRepository extends JpaRepository<Eventorganisator, Integer> {

}


