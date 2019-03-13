package com.lambdaschool.restfulcars;

import lombok.Data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A formatted log message
 */
@Data
public class Log implements Serializable {
  private final String MSG;
  private final String DATE;

  /**
   * Constructor. Creates a log message with a date.
   *
   * @param msg Message to be logged
   */
  public Log(String msg) {
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    this.MSG = msg;
    this.DATE = dateFormat.format(date);
  }
}
