package com.seta.remote.interview.service;

import com.seta.remote.interview.models.City;

import java.util.List;

public interface CityDao {
    City findCityById(int id);

    City removeCity(City city);

    City addCity(City city);

    City updateCity(City city);

    List<City> findAllCities();

    List<City> findCitiesByCountryCode(String countryCode);
}
