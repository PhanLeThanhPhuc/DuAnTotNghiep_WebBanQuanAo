package com.poly.elnr.repository;

import com.poly.elnr.dto.OrderDTO;
import com.poly.elnr.dto.PhoneTotalDTO;
import com.poly.elnr.dto.TotalWithUserOrderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Order;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

    @Query("SELECT o FROM Order o WHERE o.user.id =:userId")
    List<Order> findOrderByIdUser(@Param("userId") int userId);

//    @Query("SELECT new com.poly.elnr.dto.OrderDTO(o.total, o.orderDate) from Order o group by o.orderDate")
//    List<OrderDTO> findAllTotal();

    @Query("SELECT new com.poly.elnr.dto.OrderDTO(SUM(o.total), o.orderDate) FROM Order o GROUP BY o.orderDate")
    List<OrderDTO> findAllTotal();

    @Query("SELECT new com.poly.elnr.dto.TotalWithUserOrderDTO(o.phone, SUM(o.total) , CAST(o.orderDate AS date)) " +
            "FROM Order o " +
            "GROUP BY o.phone, CAST(o.orderDate AS date)")
    List<TotalWithUserOrderDTO> findTotalByPhoneAndDateRange();

    @Query("SELECT new com.poly.elnr.dto.PhoneTotalDTO(o.phone, SUM(o.total), COUNT(o.phone)) " +
            "FROM Order o " +
            "WHERE MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY o.phone " +
            "ORDER BY SUM(o.total) DESC " +
            "LIMIT 10")
    List<PhoneTotalDTO> findTop10ByPhonePriceWithDate();

}
