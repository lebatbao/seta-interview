package com.seta.remote.interview;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.seta.remote.interview.models.entity.Order;
import com.seta.remote.interview.models.entity.Product;
import com.seta.remote.interview.repos.OrderRepo;
import com.seta.remote.interview.repos.ProductRepo;

@Slf4j
@Component
public class AppCommandRunner implements CommandLineRunner {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    OrderRepo orderRepo;

    @Override
    public void run(String... args ) throws Exception {
        log.info("running runner");
        basicStreamApi();
        advanceStreamApi();

    }

    private void basicStreamApi() {
//        1 — Obtain a list of products belongs to category “Books” with price > 100
            log.info("\n1 _ Obtain a list of products belongs to category (Books) with price > 100 \n");
            List<Product> booksWithPricesOver100 = productRepo.findByCategoryAndPriceGreaterThan("Books", 100.00);
            booksWithPricesOver100.forEach(product -> {
                log.info(product.toString());
            });


//        2 — Obtain a list of order with products belong to category “Baby”
            log.info("\n2 _ Obtain a list of order with products belong to category (Baby)\n");
            List<Product> babyProduct = productRepo.findByCategory("Baby");
            babyProduct.forEach(product -> {
                log.info(product.toString());
            });

//        3 — Obtain a list of product with category = “Toys” and then apply 10% discount
            log.info("\n3 _ Obtain a list of product with category = (Toys) and then apply 10% discount)\n");
            List<Product> toysProduct = productRepo.findByCategory("Toys");
            log.info("\nbefore discount\n");
            toysProduct.forEach(product -> {
                log.info(product.toString());
                productRepo.TenPercentDiscount(product.getId(),(product.getPrice() - product.getPrice()/10));
            });
            log.info("\nafter discount\n");
            toysProduct = productRepo.findByCategory("Toys");
            toysProduct.forEach(product -> {
                log.info(product.toString());
            });

//        4 — Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021
            log.info("\n4 _ Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021)\n");
            List<Order> ordersBetween = orderRepo.findByOrderDateBetweenAndCustomerTier(LocalDate.of(2021, 2, 1), LocalDate.of(2021, 4, 1),2);
            ordersBetween.forEach(order -> {
                log.info(order.toString());
            });

//        5 — Get the cheapest products of “Books” category
            log.info("\n5 _ Get the cheapest products of (Books) category\n");
            Product cheapestBooks = productRepo.findFirstByCategoryOrderByPriceAsc("Books");
            log.info(cheapestBooks.toString());

//        6 — Get the 3 most recent placed order
            log.info("\n6 _ Get the 3 most recent placed order\n");  
            List<Order> ordersRecent = orderRepo.findFirst3ByOrderByOrderDateDesc();
            ordersRecent.forEach(order -> {
                log.info(order.toString());
            });

//        7 — Get a list of orders which were ordered on 15-Mar-2021, log the order records to the console and then return its product list
            log.info("\n7 _ Get a list of orders which were ordered on 15-Mar-2021, log the order records to the console and then return its product list\n");  
            List<Order> ordersOnDate = orderRepo.findByOrderDate(LocalDate.of(2021, 3, 15));
            List<Product> productList = new ArrayList<>();
            ordersOnDate.forEach(order -> {
                log.info(order.toString());
                productList.addAll(order.getProducts());
            });
            log.info("\n\nproduct list\n");
            productList.forEach(product -> {
                log.info(product.toString());
            });

//        8 — Calculate total lump sum of all orders placed in Feb 2021
            log.info("\n8 _ Calculate total lump sum of all orders placed in Feb 2021\n"); 
            ordersBetween = orderRepo.findByOrderDateBetween(LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 28));
            double lumpsum = 0;
            productList.clear();
            ordersBetween.forEach(order -> {
                productList.addAll(order.getProducts());
            });
            for(Product product: productList){
                lumpsum = lumpsum + product.getPrice();
            }
            log.info("lumpsum: " + lumpsum);

//        9 — Calculate order average payment placed on 14-Mar-2021
            log.info("\n9 _ Calculate order average payment placed on 14-Mar-2021\n"); 
            ordersOnDate = orderRepo.findByOrderDate(LocalDate.of(2021, 3, 14));
            productList.clear();
            
            if(ordersOnDate.size() == 0){
                log.info("average order payment: 0");
            } else {
                ordersOnDate.forEach(order -> {
                    productList.addAll(order.getProducts());
                });
                lumpsum = 0;
                for(Product product: productList){
                    lumpsum = lumpsum + product.getPrice();
                }
                log.info("average order payment: " + lumpsum/ordersOnDate.size());
            }
            
//        10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category “Books”
            log.info("\n10 _ Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category (Books)\n");
            List<Product> booksProduct = productRepo.findByCategory("Books");
            double sum = 0;
            double avg = 0;
            for(Product product: booksProduct){
                sum = sum + product.getPrice();
            }
            avg = sum/booksProduct.size();
            log.info("sum: " + sum);
            log.info("avg: " + avg);
            log.info("max: " + productRepo.findFirstByCategoryOrderByPriceDesc("Books").getPrice());
            log.info("min: " + productRepo.findFirstByCategoryOrderByPriceAsc("Books").getPrice());
            log.info("count: " + booksProduct.size());

//        11 — Obtain a data map with order id and order’s product count
            log.info("\n11 _ Obtain a data map with order id and order product count\n");
            HashMap<Long,Integer> dataMap = new HashMap<>();
            List<Order> orderList = orderRepo.findAll();
            for(Order order : orderList){
                dataMap.put(order.getId(), order.getProducts().size());
            }

            for (Map.Entry<Long, Integer> set : dataMap.entrySet()) {
            System.out.println("Order id: " + set.getKey() + " Product count: " + set.getValue());
            }
            
//        12 — Produce a data map with order records grouped by customer
            log.info("\n12 _ Produce a data map with order records grouped by customer\n");  

//        13 — Produce a data map with order record and product total sum
//        14 — Obtain a data map with list of product name by category
//        15 — Get the most expensive product by category
    }

    private void advanceStreamApi() {
//        Find the highest populated city of each country:
//        Find the most populated city of each continent:
//        Find the number of movies of each director: Try to solve this problem by assuming that Director class has not the member movies.
//        Find the number of genres of each director's movies:
//        Find the highest populated capital city:
//        Find the highest populated capital city of each continent:
//        Sort the countries by number of their cities in desending order:
//        Find the list of movies having the genres "Drama" and "Comedy" only:
//        Group the movies by the year and list them:
//        Sort the countries by their population densities in descending order ignoring zero population countries:
    }

}
