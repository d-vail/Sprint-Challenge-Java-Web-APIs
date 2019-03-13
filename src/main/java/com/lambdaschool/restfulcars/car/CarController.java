package com.lambdaschool.restfulcars.car;

import com.lambdaschool.restfulcars.Log;
import com.lambdaschool.restfulcars.RestfulcarsApplication;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A REST Controller for the Car table
 */
@Slf4j
@RestController
@RequestMapping("/cars")
public class CarController {
  private final CarRepository CAR_REPO;
  private final RabbitTemplate RBMQ_TEMPLATE;
  private final CarResourceAssembler ASSEMBLER;

  /**
   * Constructor
   *
   * @param carRepo         An instance of the Car database interface for querying
   * @param rabbitTemplate  An instance of a RabbitMQ Template for messages
   */
  public CarController(CarRepository carRepo, RabbitTemplate rabbitTemplate,
                       CarResourceAssembler assembler) {
    this.CAR_REPO = carRepo;
    this.RBMQ_TEMPLATE = rabbitTemplate;
    this.ASSEMBLER = assembler;
  }

  /**
   * Create a car record.
   *
   * @param car                           A JSON object with the new car
   * @return                              The car that was created
   * @throws ConstraintViolationException When attempting to create a duplicate record
   * @throws URISyntaxException           When there is an error in the link creation
   */
  @PostMapping("")
  public ResponseEntity<?> create(@RequestBody Car car) throws URISyntaxException {
    Resource<Car> carResource = ASSEMBLER.toResource(CAR_REPO.save(car));
    Log msg = new Log("Created a car");
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());
    return ResponseEntity.created(new URI(carResource.getId().expand().getHref()))
            .body(carResource);
  }

  /**
   * Load multiple sets of data from the request body.
   *
   * @param newCars                       A list of car data
   * @return                              The list of cars that was successfully saved
   * @throws URISyntaxException           When there is an error in the link creation
   */
  @PostMapping("/upload")
  public ResponseEntity<?> upload(@RequestBody List<Car> newCars) throws URISyntaxException {
    List<Resource<Car>> cars = CAR_REPO.saveAll(newCars).stream()
            .map(ASSEMBLER::toResource)
            .collect(Collectors.toList());

    Resources<Resource<Car>> carResources = new Resources<>(cars,
            linkTo(methodOn(CarController.class).findAll()).withRel("cars"));

    Log msg = new Log("Data loaded");
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());

    return ResponseEntity.created(new URI("/cars")).body(carResources);
  }

  /**
   * Find all cars.
   *
   * @return  A list of all existing cars
   */
  @GetMapping()
  public Resources<Resource<Car>> findAll() {
    List<Resource<Car>> cars = CAR_REPO.findAll().stream()
            .map(ASSEMBLER::toResource)
            .collect(Collectors.toList());

    Log msg = new Log("Search for all cars");
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());

    return new Resources<>(cars,
            linkTo(methodOn(CarController.class).findAll()).withSelfRel());
  }

  /**
   * Find car by id.
   *
   * @param id                    The car id
   * @return                      The car record matching the given id
   * @throws CarNotFoundException When invalid car id is given
   */
  @GetMapping("/id/{id}")
  public Resource<Car> findById(@PathVariable Long id) {
    Car car = CAR_REPO.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    Log msg = new Log("Search for car id " + id);
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());
    return ASSEMBLER.toResource(car);
  }

  /**
   * Find cars by year.
   *
   * @param year  The car production year
   * @return      A list of cars matching the given production year
   */
  @GetMapping("/year/{year}")
  public Resources<Resource<Car>> findByYear(@PathVariable int year) {
    List<Resource<Car>> cars = CAR_REPO.findByYear(year).stream()
            .map(ASSEMBLER::toResource)
            .collect(Collectors.toList());

    Log msg = new Log("Search for cars produced in " + year);
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());

    return new Resources<>(cars,
            linkTo(methodOn(CarController.class).findByYear(year)).withSelfRel());
  }

  /**
   * Find cars by brand.
   *
   * @param brand The car brand
   * @return      A list of cars matching the given car brand.
   */
  @GetMapping("/brand/{brand}")
  public Resources<Resource<Car>> findByBrand(@PathVariable String brand) {
    List<Resource<Car>> cars = CAR_REPO.findByBrand(brand).stream()
            .map(ASSEMBLER::toResource)
            .collect(Collectors.toList());

    Log msg = new Log("Search for " + brand);
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());

    return new Resources<>(cars,
            linkTo(methodOn(CarController.class).findByBrand(brand)).withSelfRel());
  }

  /**
   * Delete car by id.
   *
   * @param id                              The car id
   * @return                                A response body with No Content status
   * @throws EmptyResultDataAccessException When invalid car id is given
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteById(@PathVariable Long id) {
    CAR_REPO.deleteById(id);
    Log msg = new Log("Car id " + id + " data deleted");
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());
    return ResponseEntity.noContent().build();
  }
}
