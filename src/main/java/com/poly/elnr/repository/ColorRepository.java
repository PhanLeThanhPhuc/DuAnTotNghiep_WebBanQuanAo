package com.poly.elnr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer>{

	@Query("SELECT c.id from Color c")
	List<Integer> findAllColorId();
	
}
