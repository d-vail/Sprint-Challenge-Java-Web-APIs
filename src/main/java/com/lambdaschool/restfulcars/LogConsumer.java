package com.lambdaschool.restfulcars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * A Spring service - Listener for Log queue
 */
@Slf4j
@Service
public class LogConsumer {
  /**
   * Logs any message received on the Log queue
   *
   * @param msg An AMQP type message
   */
  @RabbitListener(queues = RestfulcarsApplication.QUEUE)
  public void consumeLog(final Message msg) {
    log.info("Message Received: {}", msg.toString());
  }
}
