package com.lambdaschool.restfulcars.car;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Converts a Car object to a Car resource
 */
@Component
public class CarResourceAssembler implements ResourceAssembler<Car, Resource<Car>> {
  /**
   * Convert a car object into a resource based object.
   *
   * @param car A car object
   * @return    A resource based object
   */
  @Override
  public Resource<Car> toResource(Car car) {
    return new Resource<Car>(car,
            linkTo(methodOn(CarController.class).findById(car.getId())).withSelfRel(),
            linkTo(methodOn(CarController.class).findAll()).withRel("cars"));
  }
}
