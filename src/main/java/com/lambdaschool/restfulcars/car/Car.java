package com.lambdaschool.restfulcars.car;

import lombok.Data;

import javax.persistence.*;

/**
 * A car model
 */
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"year", "brand", "model"})})
public class Car {
  private @Id @GeneratedValue Long id;
  private int year;
  private String brand;
  private String model;

  /**
   * Default Constructor. Needed for JPA.
   */
  public Car() {}

  /**
   * Additional Constructor. Creates a car object.
   *
   * @param year  The year the car was produced
   * @param brand The car manufacturer
   * @param model The car model name
   */
  public Car(int year, String brand, String model) {
    this.year = year;
    this.brand = brand;
    this.model = model;
  }
}
