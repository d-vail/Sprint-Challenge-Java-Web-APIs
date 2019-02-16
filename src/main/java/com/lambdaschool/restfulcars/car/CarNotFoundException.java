package com.lambdaschool.restfulcars.car;

public class CarNotFoundException extends RuntimeException {
  public CarNotFoundException(Long id) {
    super(String.format("Could not find car id %d", id));
  }
}
