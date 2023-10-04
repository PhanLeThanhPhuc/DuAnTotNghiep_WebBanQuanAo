package com.poly.elnr.repository;

import java.util.List;

//import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.poly.elnr.dto.CategoryDTO;
import com.poly.elnr.entity.CategoryDetail;


@Repository
public interface CategoryDetailRepository extends JpaRepository<CategoryDetail, Long> {
	
	@Query("select new com.poly.elnr.dto.CategoryDTO(cd.id, cd.name, cd.category.name) from CategoryDetail cd")
	List<CategoryDTO> findCategoryAndCategoryDetail();
	
}
