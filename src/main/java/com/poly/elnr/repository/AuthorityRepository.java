package com.poly.elnr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Authority;
import com.poly.elnr.entity.Users;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer>{
	
	
	@Query("SELECT DISTINCT a FROM Authority a WHERE a.user IN ?1")
	List<Authority> authoritiesOf(List<Users> users);


}
