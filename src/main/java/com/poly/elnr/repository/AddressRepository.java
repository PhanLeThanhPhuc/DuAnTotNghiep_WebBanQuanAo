package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Address;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

    @Query("SELECT a FROM Address a WHERE a.user.id =:userId")
    List<Address> findAddressByIdUser(@Param("userId") int userId);

//    void delete(int id);
}
