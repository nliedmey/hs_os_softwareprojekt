package de.swprojekt.speeddating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.swprojekt.speeddating.model.Studierender;

@Repository
public interface IStudierenderRepository extends JpaRepository<Studierender,Integer> {

}
