package com.lambdaschool.restfulcars.car;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST Controller for the Car table
 */
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
}
