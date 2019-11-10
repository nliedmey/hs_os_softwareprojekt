package de.swprojekt.speeddating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.swprojekt.speeddating.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.username=:username")
	User findByUsername(@Param("username") String username);

}
