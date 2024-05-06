package com.seta.remote.interview;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.seta.remote.interview.models.entity.Customer;
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
            List<Product> cheapestBooks = productRepo.findByCategory("Baby");
            cheapestBooks.forEach(product -> {
                log.info(product.toString());
            });
//        6 — Get the 3 most recent placed order
//        7 — Get a list of orders which were ordered on 15-Mar-2021, log the order records to the console and then return its product list
//        8 — Calculate total lump sum of all orders placed in Feb 2021
//        9 — Calculate order average payment placed on 14-Mar-2021
//        10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category “Books”
//        11 — Obtain a data map with order id and order’s product count
//        12 — Produce a data map with order records grouped by customer
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
