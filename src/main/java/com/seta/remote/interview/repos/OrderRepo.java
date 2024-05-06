package com.seta.remote.interview.repos;

import java.time.LocalDate;
import java.util.List;

import com.seta.remote.interview.models.entity.Customer;
import com.seta.remote.interview.models.entity.Order;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {

	List<Order> findAll();
	//4
	@Query("SELECT o FROM Order o WHERE o.orderDate between :firstDate and :secondDate and o.customer.tier = tier")
	List<Order> findByOrderDateBetweenAndCustomerTier(@Param("firstDate")LocalDate firstDate,@Param("secondDate")LocalDate secondDate,@Param("tier")int tier);
}
