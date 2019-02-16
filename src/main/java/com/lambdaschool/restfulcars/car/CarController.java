package com.lambdaschool.restfulcars.car;

import com.lambdaschool.restfulcars.Log;
import com.lambdaschool.restfulcars.RestfulcarsApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

/**
 * A REST Controller for the Car table
 */
@Slf4j
@RestController
@RequestMapping("/cars")
public class CarController {
  private final CarRepository CAR_REPO;
  private final RabbitTemplate RBMQ_TEMPLATE;

  /**
   * Constructor
   *
   * @param carRepo         An instance of the Car database interface for querying
   * @param rabbitTemplate  An instance of a RabbitMQ Template for messages
   */
  public CarController(CarRepository carRepo, RabbitTemplate rabbitTemplate) {
    this.CAR_REPO = carRepo;
    this.RBMQ_TEMPLATE = rabbitTemplate;
  }

  /**
   * Load multiple sets of data from the request body.
   *
   * @param cars  A list of car data
   * @return      The list of cars that was successfully saved
   */
  @PostMapping("/upload")
  public List<Car> upload(@RequestBody List<Car> cars) {
    Log msg = new Log("Data loaded");
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());
    return CAR_REPO.saveAll(cars);
  }

  /**
   * Find car by id.
   *
   * @param id                    The car id
   * @return                      The car record matching the given id
   * @throws CarNotFoundException When invalid car id is given
   */
  @GetMapping("/id/{id}")
  public Car findById(@PathVariable Long id) {
    Log msg = new Log("Search for car id " + id);
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());
    return CAR_REPO.findById(id).orElseThrow(() -> new CarNotFoundException(id));
  }

  /**
   * Find cars by year.
   *
   * @param year  The car production year
   * @return      A list of cars matching the given production year
   */
  @GetMapping("/year/{year}")
  public List<Car> findByYear(@PathVariable int year) {
    Log msg = new Log("Search for cars produced in " + year);
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());
    return CAR_REPO.findByYear(year);
  }

  /**
   * Find cars by brand.
   *
   * @param brand The car brand
   * @return      A list of cars matching the given car brand.
   */
  @GetMapping("/brand/{brand}")
  public List<Car> findByBrand(@PathVariable String brand) {
    Log msg = new Log("Search for " + brand);
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());
    return CAR_REPO.findByBrand(brand);
  }

  /**
   * Delete car by id.
   *
   * @param id                              The car id
   * @throws EmptyResultDataAccessException When invalid car id is given
   */
  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable Long id) {
    CAR_REPO.deleteById(id);
    Log msg = new Log("Car id " + id + " data deleted");
    RBMQ_TEMPLATE.convertAndSend(RestfulcarsApplication.QUEUE, msg.toString());
  }
}
