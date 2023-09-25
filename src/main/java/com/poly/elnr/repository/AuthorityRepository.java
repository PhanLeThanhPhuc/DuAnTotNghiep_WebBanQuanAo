package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer>{

}
