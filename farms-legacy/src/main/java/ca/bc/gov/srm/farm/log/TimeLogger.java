/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.log;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TimeLogger.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
public class TimeLogger {

  /** log. */
  private Logger log = null;

  /** running. */
  private boolean running = false;

  /** startTime. */
  private long startTime = -1;

  /** stopTime. */
  private long stopTime = -1;

  /** task. */
  private String task = null;


  /**
   * Creates a new TimeLogger object.
   *
   * @param  aTask  Input parameter to initialize class.
   */
  public TimeLogger(final String aTask) {
    this.log = LoggerFactory.getLogger(TimeLogger.class);
    this.task = aTask;
  }


  /**
   * Creates a new TimeLogger object.
   *
   * @param  aLog   log Input parameter to initialize class.
   * @param  aTask  Input parameter to initialize class.
   */
  public TimeLogger(final Logger aLog, final String aTask) {
    this.log = aLog;
    this.task = aTask;
  }


  /**
   * getElapsedTime.
   *
   * @return  The return value.
   */
  public long getElapsedTime() {
    long result = 0;

    if (startTime == -1) {
      result = 0;
    }

    if (running) {
      result = System.currentTimeMillis() - startTime;
    } else {
      result = stopTime - startTime;
    }

    return result;
  }


  /** reset. */
  public void reset() {
    startTime = -1;
    stopTime = -1;
    running = false;
  }


  /** start. */
  public void start() {

    if (!running) {
      startTime = System.currentTimeMillis();
    }

    running = true;
  }

  /** stop. */
  public void stop() {

    if (running) {
      stopTime = System.currentTimeMillis();
      logStop();
    }

    running = false;
  }


  /**
   * stop.
   *
   * @param  aTask  The parameter value.
   */
  public void stop(final String aTask) {
    this.task = aTask;

    if (running) {
      stopTime = System.currentTimeMillis();
      logStop();
    }

    running = false;
  }


  /**
   * toString.
   *
   * @return  The return value.
   */
  @Override
  public String toString() {
    long millis = getElapsedTime();

    return millisecondsToString(millis);
  }


  /** logStop. */
  protected void logStop() {

    if (StringUtils.isNotBlank(task)) {
      String msg = task + " - " + toString();
      log(msg);
    }
  }

  /**
   * millisecondsToString.
   *
   * @param   time  The parameter value.
   *
   * @return  The return value.
   */
  protected String millisecondsToString(final long time) {
    final int thousand = 1000;
    final int sixty = 60;
    final int twoFour = 24;
    final int ten = 10;
    final int cent = 100;
    final int sixtyK = 60000;
    final int threeSixtyK = 3600000;
    int milliseconds = (int) (time % thousand);
    int seconds = (int) ((time / thousand) % sixty);
    int minutes = (int) ((time / sixtyK) % sixty);
    int hours = (int) ((time / threeSixtyK) % twoFour);
    String millisecondsStr = "000";

    if (milliseconds < ten) {
      millisecondsStr = "00" + milliseconds;
    } else if (milliseconds < cent) {
      millisecondsStr = "0" + milliseconds;
    } else {
      millisecondsStr = "" + milliseconds;
    }

    String secondsStr = "00";

    if (seconds < ten) {
      secondsStr = "0" + seconds;
    } else {
      secondsStr = "" + seconds;
    }

    String minutesStr = "00";

    if (minutes < ten) {
      minutesStr = "0" + minutes;
    } else {
      minutesStr = "" + minutes;
    }

    String hoursStr = "00";

    if (hours < ten) {
      hoursStr = "0" + hours;
    } else {
      hoursStr = "" + hours;
    }

    return new String(hoursStr + ":" + minutesStr + ":" + secondsStr + "."
        + millisecondsStr);
  }

  /**
   * log.
   *
   * @param  msg  The parameter value.
   */
  private void log(final String msg) {

    // always log at info
    if (log.isInfoEnabled()) {
      log.info(msg);
    }
  }
}
