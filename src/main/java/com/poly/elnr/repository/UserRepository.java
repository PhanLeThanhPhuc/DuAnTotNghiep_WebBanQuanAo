package com.poly.elnr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

Optional<User> findByEmail(String email);
	
	@Query("Select o FROM User o WHERE o.email = :email")
	User findEmail(@Param("email") String email);
}
