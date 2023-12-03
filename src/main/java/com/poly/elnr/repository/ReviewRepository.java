package com.poly.elnr.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.poly.elnr.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{
	
	
	@Query("select p from Review p where p.product.id=?1")
	Page<Review> findByProductID(Integer id, Pageable pageable);
	
	
	@Query("select p from Review p where p.product.id=?1")
	List<Review> findByProductID2(Integer id);

	@Query("select p from Review p where p.user.id=?1 and p.product.id=?2 ")
	Review findByProductAndUserID(Integer userid, Integer productid);


}
