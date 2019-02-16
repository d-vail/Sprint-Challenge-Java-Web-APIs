package com.lambdaschool.restfulcars.car;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CarRepository is a JPA database interface
 */
public interface CarRepository extends JpaRepository<Car, Long> {
  List<Car> findByYear(int year);
  List<Car> findByBrand(String brand);
}
