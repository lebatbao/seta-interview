package com.seta.remote.interview;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.seta.remote.interview.models.City;
import com.seta.remote.interview.models.Country;
import com.seta.remote.interview.models.Director;
import com.seta.remote.interview.models.Movie;
import com.seta.remote.interview.models.entity.Customer;
import com.seta.remote.interview.models.entity.Order;
import com.seta.remote.interview.models.entity.Product;
import com.seta.remote.interview.repos.CustomerRepo;
import com.seta.remote.interview.repos.OrderRepo;
import com.seta.remote.interview.repos.ProductRepo;
import com.seta.remote.interview.service.InMemoryMovieService;
import com.seta.remote.interview.service.InMemoryWorldDao;

@Slf4j
@Component
public class AppCommandRunner implements CommandLineRunner {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    EntityManager entityManager;

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
                log.info("Order id: " + set.getKey() + " Product count: " + set.getValue());
            }
            
//        12 — Produce a data map with order records grouped by customer
            log.info("\n12 _ Produce a data map with order records grouped by customer\n");  
            HashMap<Customer,List<Order>> dataMap12 = new HashMap<>();
            for(Customer customer : customerRepo.findAll()){
                dataMap12.put(customer,  entityManager.createQuery(
                    "select o " +
                    "from Order o " +
                    "where o.customer.id = :customerID", Order.class)
                    .setParameter( "customerID", customer.getId())
                    .getResultList());
            }
            for (Map.Entry<Customer,List<Order>> set : dataMap12.entrySet()) {
                    log.info("Customer id: " + set.getKey().getId() + " Orders: " + set.getValue().size());
                }

//        13 — Produce a data map with order record and product total sum
            log.info("\n13 _ Produce a data map with order record and product total sum\n");
            HashMap<Long,Double> dataMap13 = new HashMap<>();
            for(Order order : orderList){
                Double totalSum = 0.0;
                for(Product product : order.getProducts()){
                    totalSum += product.getPrice();
                }
                dataMap13.put(order.getId(), totalSum);
            }

            for (Map.Entry<Long, Double> set : dataMap13.entrySet()) {
                log.info("Order id: " + set.getKey() + " Product sum: " + set.getValue());
            }

//        14 — Obtain a data map with list of product name by category
            log.info("\n14 _ Obtain a data map with list of product name by category\n");  
            HashMap<String,List<Product>> dataMap14 = new HashMap<>();
            for(String category : entityManager.createQuery(
                "select DISTINCT p.category "+
                "from Product p", String.class)
            .getResultList()){
                dataMap14.put(category,  entityManager.createQuery(
                    "select p " +
                    "from Product p " +
                    "where p.category = :category", Product.class)
                    .setParameter( "category", category)
                    .getResultList());
            }
            for (Map.Entry<String,List<Product>> set : dataMap14.entrySet()) {
                    log.info("Category: " + set.getKey().toString() + " Orders: " + set.getValue().size());
                }
//        15 — Get the most expensive product by category
            log.info("\n15 _ Get the most expensive product by category\n"); 
            HashMap<String,Product> dataMap15 = new HashMap<>(); 
            for(Product product : productRepo.findAll()){
                if(dataMap15.get(product.getCategory())==null || dataMap15.get(product.getCategory()).getPrice() < product.getPrice()){
                    dataMap15.put(product.getCategory(), product);
                } 
            }
            for (Map.Entry<String,Product> set : dataMap15.entrySet()) {
                log.info("Category: " + set.getKey() + " Product: " + set.getValue());
            }
    }

    private void advanceStreamApi() {
            InMemoryWorldDao inMemoryWorldDao = new InMemoryWorldDao();
            InMemoryMovieService inMemoryMovieService = new InMemoryMovieService();

//        Find the highest populated city of each country:
            log.info("\n16 _ Find the highest populated city of each country:\n"); 
            HashMap<Country,City> dataMap16 = new HashMap<>(); 
            for(Country country : inMemoryWorldDao.findAllCountries()){
                for(City city : country.getCities()){
                    if(dataMap16.get(country)==null || dataMap16.get(country).getPopulation() < city.getPopulation()){
                        dataMap16.put(country, city);
                    } 
                }
            }

            for (Map.Entry<Country,City> set : dataMap16.entrySet()) {
                log.info("Country: " + set.getKey() + " City: " + set.getValue());
            }

//        Find the most populated city of each continent:
//unoptimal
            log.info("\n17 _ Find the most populated city of each continent:\n"); 
            HashMap<String,City> dataMap17 = new HashMap<>(); 
            
            
            for(String continents : inMemoryWorldDao.getAllContinents()){
                for (Map.Entry<Country,City> set : dataMap16.entrySet()) {
                    if(set.getKey().getContinent() == continents){
                        if(dataMap17.get(continents)==null || dataMap17.get(continents).getPopulation() < set.getValue().getPopulation()){
                            dataMap17.put(continents, set.getValue());
                        } 
                    }
                }
            }

            for (Map.Entry<String,City> set : dataMap17.entrySet()) {
                log.info("Continents: " + set.getKey() + " City: " + set.getValue());
            }

//        Find the number of movies of each director: Try to solve this problem by assuming that Director class has not the member movies.
            log.info("\n18 _ Find the number of movies of each director: Try to solve this problem by assuming that Director class has not the member movies.\n");
            HashMap<Director,Integer> directorNumberOfMovies = new HashMap<>();
            for ( Movie movie : inMemoryMovieService.findAllMovies()) {
                for(Director director : movie.getDirectors()){
                    if(directorNumberOfMovies.get(director)==null){
                        directorNumberOfMovies.put(director, 1);
                    } else {
                        directorNumberOfMovies.put(director, directorNumberOfMovies.get(director) + 1);
                    }
                }
                
            }

            for (Map.Entry<Director,Integer> set : directorNumberOfMovies.entrySet()) {
                log.info("Director: " + set.getKey() + " Number Of Movies: " + set.getValue());
            }

//        Find the number of genres of each director's movies:
            log.info("\n19 _ Find the number of genres of each director's movies:\n");
            HashMap<Director,Integer> directorNumberOfGenres = new HashMap<>();
            for ( Movie movie : inMemoryMovieService.findAllMovies()) {
                for(Director director : movie.getDirectors()){
                    if(directorNumberOfGenres.get(director)==null){
                        directorNumberOfGenres.put(director, movie.getGenres().size());
                    } else {
                        directorNumberOfGenres.put(director, directorNumberOfGenres.get(director) + movie.getGenres().size());
                    }
                }
                
            }
//        Find the highest populated capital city:
            log.info("\n20 _  Find the highest populated capital city:\n");
            City highestPopulatedCapCity = null;
            for(Country country : inMemoryWorldDao.findAllCountries()){
                if(inMemoryWorldDao.findCityById(country.getCapital()) == null){
                    continue;
                }
                if(highestPopulatedCapCity == null || highestPopulatedCapCity.getPopulation() < inMemoryWorldDao.findCityById(country.getCapital()).getPopulation()){
                    highestPopulatedCapCity = inMemoryWorldDao.findCityById(country.getCapital());
                }
            }

            log.info("" + highestPopulatedCapCity);

//        Find the highest populated capital city of each continent:

            HashMap<String,Set<City>> continentToListOfCapCountry = new HashMap<>();
            for(Country country : inMemoryWorldDao.findAllCountries()){
                if(inMemoryWorldDao.findCityById(country.getCapital()) == null){
                    continue;
                }
                if(continentToListOfCapCountry.get(country.getContinent())==null){
                    continentToListOfCapCountry.put(country.getContinent(), new HashSet<>());
                }
                
                continentToListOfCapCountry.get(country.getContinent()).add(inMemoryWorldDao.findCityById(country.getCapital()));
            }
            
            log.info("\n21 _  Find the highest populated capital city of each continent:\n");
            HashMap<String,City> dataMap21 = new HashMap<>();
            for(String continents : inMemoryWorldDao.getAllContinents()){
                if(continentToListOfCapCountry.get(continents) == null){
                    continue;
                }
                for(City city : continentToListOfCapCountry.get(continents)){
                    if(dataMap21.get(continents)==null ||dataMap21.get(continents).getPopulation()<city.getPopulation()){
                        dataMap21.put(continents, city);
                    }
                }
            }

            for (Map.Entry<String,City> set : dataMap21.entrySet()) {
                log.info("Continents: " + set.getKey() + " City: " + set.getValue());
            }

//        Sort the countries by number of their cities in desending order:
            log.info("\n22 _  Sort the countries by number of their cities in desending order:\n");
            HashMap<Country,Integer> dataMap22 = new HashMap<>();
            for(Country country : inMemoryWorldDao.findAllCountries()){
                dataMap22.put(country, country.getCities().size());
            }
            List<Map.Entry<Country,Integer>> list = new ArrayList<>(dataMap22.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<Country,Integer>>() {
                public int compare(Map.Entry<Country,Integer> o1, Map.Entry<Country,Integer> o2){
                    return o1.getValue()- o2.getValue();
                }
            });

            for(Map.Entry<Country,Integer> set: list ){
                log.info("Country: " + set.getKey() + " Number of City: " + set.getValue());
            }

//        Find the list of movies having the genres "Drama" and "Comedy" only:
            log.info("\n23 _  Find the list of movies having the genres (Drama) and (Comedy) only:\n");
            Set<Movie> dramaAndComedy = new HashSet<>();
            for(Movie movie : inMemoryMovieService.findAllMovies()){
                if(movie.getGenres().size() == 2 
                || movie.getGenres().contains(inMemoryMovieService.findGenreByName("Drama")) 
                || movie.getGenres().contains(inMemoryMovieService.findGenreByName("Comedy"))){
                    dramaAndComedy.add(movie);
                }
            }

            for(Movie movie : dramaAndComedy){
                log.info("Movie: " + movie);
            }

//        Group the movies by the year and list them:
            log.info("\n24 _ Group the movies by the year and list them:\n");
            HashMap<Integer,Set<Movie>> dataMap24 = new HashMap<>();
            for(Movie movie : inMemoryMovieService.findAllMovies()){
                if(dataMap24.get(movie.getYear())==null){
                    dataMap24.put(movie.getYear(), new HashSet<>());
                }
                dataMap24.get(movie.getYear()).add(movie);
            }
            for(Map.Entry<Integer,Set<Movie>> set : dataMap24.entrySet()){
                log.info("Year: " +  set.getKey() + "\n");
                for(Movie movie : set.getValue()){
                    log.info("movie: " + movie);
                }
            }

//        Sort the countries by their population densities in descending order ignoring zero population countries:
            log.info("\n25 _ Sort the countries by their population densities in descending order ignoring zero population countries:\n");
            List<Country> list25 = new ArrayList<>(inMemoryWorldDao.findAllCountries());
            list25.removeIf(country -> country.getPopulation() == 0);
            Collections.sort(list25, new Comparator<Country>() {
                public int compare(Country o1, Country o2){
                    return o2.getPopulation() - o1.getPopulation();
                }
            });

            for(Country country : list25 ){
                log.info("Country: " + country.getName() + " Population: " + country.getPopulation());
            }

    }

}
