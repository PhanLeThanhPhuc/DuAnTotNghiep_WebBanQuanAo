package com.poly.elnr.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Description;

@Repository
public interface DescriptionRepository extends JpaRepository<Description,Integer>{

}
