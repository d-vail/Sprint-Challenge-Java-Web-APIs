package com.lambdaschool.restfulcars.car;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CarRepository is a JPA database interface
 */
public interface CarRepository extends JpaRepository<Car, Long> {
}
