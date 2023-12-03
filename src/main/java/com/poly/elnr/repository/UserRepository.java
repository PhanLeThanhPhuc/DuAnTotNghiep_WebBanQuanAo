package com.poly.elnr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

	@Query("Select o FROM Users o WHERE o.email = :email")
	Users findByEmail(@Param("email") String email);

	@Query("Select o FROM Users o WHERE o.phone = :phone")
	Users findByPhone(@Param("phone") String phone);
	
	@Query("SELECT DISTINCT ar.user FROM Authority ar where ar.role.id IN ('ROLE_USER')")
	public List<Users> findAllUserByIdRole();

	Users findByEmailAndPhone(String email, String phone);

}
