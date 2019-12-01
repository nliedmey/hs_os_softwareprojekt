package de.swprojekt.speeddating.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.swprojekt.speeddating.model.Unternehmen;

public interface IUnternehmenRepository extends JpaRepository<Unternehmen, Integer> {

}
