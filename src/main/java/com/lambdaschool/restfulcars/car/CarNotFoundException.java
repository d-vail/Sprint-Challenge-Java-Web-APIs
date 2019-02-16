package com.lambdaschool.restfulcars.car;

/**
 * A RuntimeException to be thrown for invalid Car id's
 */
public class CarNotFoundException extends RuntimeException {
  public CarNotFoundException(Long id) {
    super(String.format("Could not find car id %d", id));
  }
}
