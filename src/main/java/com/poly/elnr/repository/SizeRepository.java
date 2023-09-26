package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer>{

}