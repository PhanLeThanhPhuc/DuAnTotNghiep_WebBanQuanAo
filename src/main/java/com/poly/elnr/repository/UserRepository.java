package com.poly.elnr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

	@Query("Select o FROM Users o WHERE o.email = :email")
	Users findEmail(@Param("email") String email);

	@Query("Select o FROM Users o WHERE o.phone = :phone")
	Users findByPhone(@Param("phone") String phone);
}
