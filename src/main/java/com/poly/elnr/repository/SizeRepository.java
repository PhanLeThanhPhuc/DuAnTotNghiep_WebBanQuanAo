package com.poly.elnr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer>{
	
	 List<Size> findByStatusTrue();
	 
	 @Query("SELECT z.id from Size z")
	 List<Integer> findAllSizeId();
	 
}
