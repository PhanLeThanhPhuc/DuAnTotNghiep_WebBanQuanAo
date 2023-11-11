package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

    @Query("SELECT o FROM Order o WHERE o.user.id =:userId")
    List<Order> findOrderByIdUser(@Param("userId") int userId);

//    List<Order> findAllOrderIdASC();

}
