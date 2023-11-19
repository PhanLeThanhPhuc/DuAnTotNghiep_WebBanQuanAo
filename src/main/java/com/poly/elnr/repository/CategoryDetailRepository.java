package com.poly.elnr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.CategoryDetail;


@Repository
public interface CategoryDetailRepository extends JpaRepository<CategoryDetail, Long> {

	@Query("select p from CategoryDetail p where p.category.id=?1")
	List<CategoryDetail> findByCategoryID(Integer id);

	
}
