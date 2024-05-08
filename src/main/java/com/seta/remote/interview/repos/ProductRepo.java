package com.seta.remote.interview.repos;

import java.util.List;

import javax.transaction.Transactional;

import com.seta.remote.interview.models.entity.Product;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends CrudRepository<Product, Long> {

	List<Product> findAll();

	//1
	List<Product> findByCategory(String catergory);
	//2
	List<Product> findByCategoryAndPriceGreaterThan(String catergory, Double Price);
	//3
	@Modifying
	@Transactional
	@Query("update Product p set p.price = :price where p.id = :id")
	void TenPercentDiscount(@Param("id") long id,@Param("price") double price);
	
	//5
	Product findFirstByCategoryOrderByPriceAsc(String catergory);

	//10
	Product findFirstByCategoryOrderByPriceDesc(String catergory);

	
}
